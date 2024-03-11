package com.example.finance_tracker.repos;

import com.example.finance_tracker.models.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, Long> {
    TransactionCategory findByName(String categoryName);
}
