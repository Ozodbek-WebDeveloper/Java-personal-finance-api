package com.example.personal_finance_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.personal_finance_api.enums.TransactionType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransactionReq(
        @NotBlank(message = "Title bo'sh bo'lmasligi kerak!") String title,

        @NotNull(message = "Amount bo'sh bo'lmasligi kerak!") @DecimalMin(value = "0.01", message = "To'lov kamida 1000 so'm bolishi kerak") BigDecimal amount,

        @NotNull(message = "transactionDate kiritilishi shart!") LocalDate transactionDate,

        @NotNull(message = "Tranzaksiya type  tanlanishi shart!") TransactionType type,

        @NotNull(message = "CategoryId bo'sh bo'lmasligi kerak") Integer categoryId) {

}
