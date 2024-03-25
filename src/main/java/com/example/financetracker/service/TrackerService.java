package com.example.financetracker.service;

import com.example.financetracker.component.Cache;
import com.example.financetracker.models.TransactionCategory;
import com.example.financetracker.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.financetracker.models.Transaction;
import com.example.financetracker.repos.TransactionRepository;
import com.example.financetracker.repos.TransactionCategoryRepository;
import com.example.financetracker.repos.UserRepository;

import java.util.List;

@Service
public class TrackerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackerService.class);

    private final TransactionRepository transactionRepository;
    private final TransactionCategoryRepository transactionCategoryRepository;
    private final UserRepository userRepository;
    private final Cache cache;

    @Autowired
    public TrackerService(TransactionRepository transactionRepository,
                          TransactionCategoryRepository transactionCategoryRepository,
                          UserRepository userRepository,
                          Cache cache) {
        this.transactionRepository = transactionRepository;
        this.transactionCategoryRepository = transactionCategoryRepository;
        this.userRepository = userRepository;
        this.cache = cache;
    }

    public String getSummaryOperations() {
        return transactionRepository.findAll().toString();
    }

    public void createTransaction(Transaction transaction) {
        LOGGER.info("Creating transaction: {}", transaction);
        transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(Long id) {
        Transaction cachedTransaction = (Transaction) cache.getFromCache(id.toString());
        if (cachedTransaction != null) {
            LOGGER.info("Retrieving transaction {} from cache", id);
            return cachedTransaction;
        } else {
            LOGGER.info("Transaction {} not found in cache, retrieving from database", id);
            Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ERROR 500 (not found)"));
            if (transaction != null) {
                LOGGER.info("Transaction {} retrieved from database, adding to cache", id);
                cache.addToCache(id.toString(), transaction);
            }
            return transaction;
        }
    }

    public void updateTransaction(Transaction transaction) {
        LOGGER.info("Updating transaction: {}", transaction);
        transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        LOGGER.info("Deleting transaction with ID: {}", id);
        transactionRepository.deleteById(id);
        cache.removeFromCache(id.toString());
    }

    public void createUser(User user) {
        LOGGER.info("Creating user: {}", user);
        userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        LOGGER.info("Retrieving user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    public TransactionCategory getCategoryByName(String tagName) {
        LOGGER.info("Retrieving category by name: {}", tagName);
        return transactionCategoryRepository.findByName(tagName);
    }

    public void createCategory(TransactionCategory tag) {
        LOGGER.info("Creating category: {}", tag);
        transactionCategoryRepository.save(tag);
    }

    public void deleteUser(Long id) {
        LOGGER.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
    }

    public List<Transaction> findTransactionsByUserUsername(String username) {
        LOGGER.info("Finding transactions by user username: {}", username);
        List<Transaction> cachedTransactions = (List<Transaction>) cache.getFromCache(username);
        if (cachedTransactions != null) {
            LOGGER.info("Retrieving transactions by user username {} from cache", username);
            return cachedTransactions;
        } else {
            LOGGER.info("Transactions by user username {} not found in cache, retrieving from database", username);
            List<Transaction> transactions = transactionRepository.findTransactionsByUserUsername(username);
            if (!transactions.isEmpty()) {
                LOGGER.info("Transactions by user username {} retrieved from database, adding to cache", username);
                cache.addToCache(username, transactions);
            }
            return transactions;
        }
    }

}
