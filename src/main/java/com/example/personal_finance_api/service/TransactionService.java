package com.example.personal_finance_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.personal_finance_api.exception.BadRequestException;
import com.example.personal_finance_api.dto.TransactionReq;
import com.example.personal_finance_api.dto.TransactionRes;
import com.example.personal_finance_api.entity.Category;
import com.example.personal_finance_api.entity.Transaction;
import com.example.personal_finance_api.exception.ResourceNotFoundException;
import com.example.personal_finance_api.mapper.TransactionMapper;
import com.example.personal_finance_api.repository.CategoryRepository;
import com.example.personal_finance_api.repository.TransactionRepository;

@Service
public class TransactionService {
  private final TransactionRepository transactionRepository;
  private final CategoryRepository categoryRepository;
  private final TransactionMapper transactionMapper;

  public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository,
      TransactionMapper transactionMapper) {
    this.transactionRepository = transactionRepository;
    this.categoryRepository = categoryRepository;
    this.transactionMapper = transactionMapper;
  }

  public TransactionRes create(TransactionReq request) {
    Category category = categoryRepository.findById(request.categoryId())
        .orElseThrow(() -> new ResourceNotFoundException("Category topilmadi: " + request.categoryId()));

    if (request.type() != category.getType()) {
      throw new BadRequestException("Transaction type  category typega mos emas!");
    }

    Transaction newTransaction = new Transaction(
        request.title(),
        request.amount(),
        request.transactionDate(),
        request.type(),
        category);

    return transactionMapper.toResponse(transactionRepository.save(newTransaction));
  }

  public TransactionRes getById(Integer id) {
    Transaction findTransaction = transactionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Transaction topilmadi:" + id));

    return transactionMapper.toResponse(findTransaction);
  }

  public TransactionRes update(Integer id, TransactionReq newTransactionReq) {
    Transaction findTransaction = transactionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Transaction topilmadi:" + id));

    Category category = categoryRepository.findById(newTransactionReq.categoryId())
        .orElseThrow(() -> new BadRequestException("Category topilmadi!"));

    if (newTransactionReq.type() != category.getType()) {
      throw new BadRequestException("Transaction type  category typega mos emas!");
    }

    findTransaction.setTitle(newTransactionReq.title());
    findTransaction.setAmount(newTransactionReq.amount());
    findTransaction.setTransactionDate(newTransactionReq.transactionDate());
    findTransaction.setType(newTransactionReq.type());
    findTransaction.setCategory(category);

    Transaction transaction = transactionRepository.save(findTransaction);

    return transactionMapper.toResponse(transaction);
  }

  public String delete(Integer id) {
    transactionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Transaction topilmadi: " + id));
    transactionRepository.deleteById(id);
    return "Transaction muvofaqiyatli o'chirildi." + id;
  }

  public List<TransactionRes> getAllList() {
    return transactionRepository.findAll().stream()
        .map(transactionMapper::toResponse)
        .toList();

  }

  public List<TransactionRes> getAllByCategory(Integer id) {
    if (!categoryRepository.findById(id).isPresent()) {
      throw new ResourceNotFoundException("Categorya topilmadi!");
    }

    return transactionRepository.findAllByCategoryId(id).stream()
        .map(transactionMapper::toResponse)
        .toList();
  }
}
