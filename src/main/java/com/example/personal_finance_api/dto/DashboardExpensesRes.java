package com.example.personal_finance_api.dto;

import java.math.BigDecimal;

public record DashboardExpensesRes(
        Integer categoryId,
        String categoryName,
        BigDecimal totalAmount) {

}
