package com.example.personal_finance_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.personal_finance_api.entity.Category;
import com.example.personal_finance_api.enums.TransactionType;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByNameIgnoreCase(String name);
    List<Category> findAllByType(TransactionType type);
}
