package com.example.personal_finance_api.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.example.personal_finance_api.dto.TransactionReq;
import com.example.personal_finance_api.dto.TransactionRes;
import com.example.personal_finance_api.service.TransactionService;

@RestController
@RequestMapping("/api/v1")
public class TransactionController {
  private final TransactionService transactionService;
  

  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @PostMapping("/transaction")
  public TransactionRes create(@Valid @RequestBody TransactionReq transactionReq) {
    return transactionService.create(transactionReq);
  }

  @GetMapping("/transaction/{id}")
  public TransactionRes getById(@PathVariable Integer id) {
    return transactionService.getById(id);
  }

  @PutMapping("/transaction/{id}")
  public TransactionRes update(@PathVariable Integer id, @Valid @RequestBody TransactionReq transactionReq) {
    return transactionService.update(id, transactionReq);
  }

  @DeleteMapping("/transaction/{id}")
  public String delete(@PathVariable Integer id) {
    return transactionService.delete(id);
  }

  @GetMapping("/transaction")
  public List<TransactionRes> getAll() {
    return transactionService.getAllList();
  }

  @GetMapping("/category/{id}/transactions")
  public List<TransactionRes> getByCategory(@PathVariable Integer id) {
    return transactionService.getAllByCategory(id);
  }
}
