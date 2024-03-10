package com.example.finance_tracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.finance_tracker.models.Transaction;
import com.example.finance_tracker.repos.TransactionRepository;

@Service
public class TrackerService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TrackerService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
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

}
