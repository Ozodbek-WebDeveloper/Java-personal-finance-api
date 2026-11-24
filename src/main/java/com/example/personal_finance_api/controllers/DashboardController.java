package com.example.personal_finance_api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.personal_finance_api.dto.DashboardExpensesRes;
import com.example.personal_finance_api.dto.DashboardSummaryRes;
import com.example.personal_finance_api.dto.TransactionRes;
import com.example.personal_finance_api.enums.TransactionType;
import com.example.personal_finance_api.service.DashboardService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public DashboardSummaryRes getSummary(@RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to) {
        return dashboardService.getSummary(from, to);
    }

    @GetMapping("/category-summary")
    public List<DashboardExpensesRes> getCategorySummary(@RequestParam TransactionType type,
            @RequestParam(required = false) LocalDate from, @RequestParam(required = false) LocalDate to) {
        return dashboardService.getCategorySummary(type, from, to);
    }

    @GetMapping("/monthly")
    public List<TransactionRes> getMonthSummary(@RequestParam(required = false) YearMonth month) {
        if (month == null) {
            month = YearMonth.now();
        }
        return dashboardService.getMonthlySummary(month);
    }

    @GetMapping("/recent-transactions")
    public List<TransactionRes> getRecentTransaction() {
        return dashboardService.getRecentTransactions();
    }
}
