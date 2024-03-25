package com.example.financetracker.repos;

import com.example.financetracker.models.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, Long> {
    TransactionCategory findByName(String categoryName);
}
