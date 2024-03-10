package com.example.finance_tracker.repos;

import com.example.finance_tracker.models.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

}
