package com.example.personal_finance_api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import com.example.personal_finance_api.exception.BadRequestException;
import org.springframework.stereotype.Service;

import com.example.personal_finance_api.dto.DashboardExpensesRes;
import com.example.personal_finance_api.dto.DashboardSummaryRes;
import com.example.personal_finance_api.dto.TransactionRes;
import com.example.personal_finance_api.enums.TransactionType;
import com.example.personal_finance_api.repository.TransactionRepository;

@Service
public class DashboardService {
  private final TransactionRepository transactionRepository;

  public DashboardService(TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  public DashboardSummaryRes getSummary(LocalDate from, LocalDate to) {

    if (from == null) {
      LocalDate oldestDate = transactionRepository.findOldestTransactionDate().orElse(null);

      if (oldestDate == null) {
        return new DashboardSummaryRes(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
      }

      from = oldestDate;
    }

    if (to == null) {
      to = LocalDate.now();
    }

    if (from.isAfter(to)) {
      throw new BadRequestException("Boshlanish sanasi tugash sanasidan katta bo'lishi mumkin emas!");
    }

    BigDecimal totalIncome = transactionRepository.sumByTypeAndDateBetween(TransactionType.INCOME, from, to);

    BigDecimal totalExpense = transactionRepository.sumByTypeAndDateBetween(TransactionType.EXPENSE, from, to);

    BigDecimal balance = totalIncome.subtract(totalExpense);

    return new DashboardSummaryRes(
        totalIncome,
        totalExpense,
        balance);

  }

  public List<DashboardExpensesRes> getCategorySummary(TransactionType type, LocalDate from, LocalDate to) {
    if (from == null) {
      LocalDate oldestDate = transactionRepository.findOldestTransactionDate().orElse(null);

      if (oldestDate == null) {
        return List.of();
      }

      from = oldestDate;
    }

    if (to == null) {
      to = LocalDate.now();
    }

    if (from.isAfter(to)) {
      throw new BadRequestException("Boshlanish sanasi tugash sanasidan katta bo'lishi mumkin emas!");
    }

    return transactionRepository.findCategoryExpenses(type, from, to);

  }

  public List<TransactionRes> getMonthlySummary(YearMonth month) {
    LocalDate from = month.atDay(1);
    LocalDate to = month.atEndOfMonth();

    return transactionRepository.findAllByTransactionDateBetween(from, to);
  }

  public  List<TransactionRes> getRecentTransactions(){
    return transactionRepository.findTop10ByOrderByTransactionDateDesc();
  }

}
