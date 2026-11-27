package com.example.personal_finance_api.dto;

import java.time.LocalDateTime;

import com.example.personal_finance_api.enums.TransactionType;

public record CategoryRes(
    Integer id,
		Integer userId,
    String name,
    TransactionType type,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
