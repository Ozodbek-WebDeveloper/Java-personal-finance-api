package com.example.personal_finance_api.dto;

import java.math.BigDecimal;

public record DashboardSummaryRes(
    BigDecimal totalIncome,
    BigDecimal totalExpense,
    BigDecimal balance) {

}
