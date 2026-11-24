package com.example.personal_finance_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.personal_finance_api.enums.TransactionType;

public record TransactionRes(
        Integer id,
        Integer categoryId,
        String title,
        BigDecimal amount,
        LocalDate transactionDate,
        TransactionType type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

}
