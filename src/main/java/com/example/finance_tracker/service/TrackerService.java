package com.example.finance_tracker.service;

import com.example.finance_tracker.models.TransactionCategory;
import com.example.finance_tracker.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.finance_tracker.models.Transaction;
import com.example.finance_tracker.repos.TransactionRepository;
import com.example.finance_tracker.repos.TransactionCategoryRepository;
import com.example.finance_tracker.repos.UserRepository;

@Service
public class TrackerService {

    private final TransactionRepository transactionRepository;
    private final TransactionCategoryRepository transactionCategoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public TrackerService(TransactionRepository transactionRepository,
                          TransactionCategoryRepository transactionCategoryRepository,
                          UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionCategoryRepository = transactionCategoryRepository;
        this.userRepository = userRepository;
    }

    public String getSummaryOperations() {
        return transactionRepository.findAll().toString();
    }

    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public void updateTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    public void createUser(User user){
        userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public TransactionCategory getCategoryByName(String tagName) {
        return transactionCategoryRepository.findByName(tagName);
    }

    public void createCategory(TransactionCategory tag) {
        transactionCategoryRepository.save(tag);
    }
    // Дополнительные методы для работы с другими сущностями, если необходимо
}

