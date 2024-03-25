package com.example.financetracker.repos;

import com.example.financetracker.models.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t "
            + "JOIN FETCH t.user u "
            + "JOIN FETCH t.category c "
            + "WHERE u.username = :username")
    List<Transaction> findTransactionsByUserUsername(String username);
}
