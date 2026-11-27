package com.example.personal_finance_api.mapper;

import org.springframework.stereotype.Component;

import com.example.personal_finance_api.dto.TransactionRes;
import com.example.personal_finance_api.entity.Transaction;

@Component
public class TransactionMapper {
  public TransactionRes toResponse(Transaction transaction) {
    return new TransactionRes(
        transaction.getId(),
        transaction.getCategory().getId(),
				transaction.getUser().getId(),
        transaction.getTitle(),
        transaction.getAmount(),
        transaction.getTransactionDate(),
        transaction.getType(),
        transaction.getCreatedAt(),
        transaction.getUpdatedAt());
  }
}
