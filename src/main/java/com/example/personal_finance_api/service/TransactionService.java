package com.example.personal_finance_api.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.personal_finance_api.exception.BadRequestException;
import com.example.personal_finance_api.dto.TransactionReq;
import com.example.personal_finance_api.dto.TransactionRes;
import com.example.personal_finance_api.entity.Category;
import com.example.personal_finance_api.entity.Transaction;
import com.example.personal_finance_api.entity.User;
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
		User user = getCurrentUser();
		Category category = categoryRepository.findByIdAndUserId(request.categoryId(), user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Category topilmadi: " + request.categoryId()));

		if (request.type() != category.getType()) {
			throw new BadRequestException("Transaction type  category typega mos emas!");
		}

		Transaction newTransaction = new Transaction(
				request.title(),
				request.amount(),
				request.transactionDate(),
				request.type(),
				category,
				user);
		return transactionMapper.toResponse(transactionRepository.save(newTransaction));
	}

	public TransactionRes getById(Integer id) {
		User user = getCurrentUser();
		Transaction findTransaction = transactionRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Transaction topilmadi:" + id));

		return transactionMapper.toResponse(findTransaction);
	}

	public TransactionRes update(Integer id, TransactionReq newTransactionReq) {
		User user = getCurrentUser();
		Transaction findTransaction = transactionRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Transaction topilmadi:" + id));

		Category category = categoryRepository.findByIdAndUserId(newTransactionReq.categoryId(), user.getId())
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
		User user = getCurrentUser();
		Transaction transaction = transactionRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Transaction topilmadi: " + id));
		transactionRepository.delete(transaction);
		return "Transaction muvofaqiyatli o'chirildi." + id;
	}

	public List<TransactionRes> getAllList() {
		User user = getCurrentUser();
		return transactionRepository.findAllByUserId(user.getId()).stream()
				.map(transactionMapper::toResponse)
				.toList();

	}

	public List<TransactionRes> getAllByCategory(Integer id) {
		User user = getCurrentUser();
		if (!categoryRepository.findByIdAndUserId(id, user.getId()).isPresent()) {
			throw new ResourceNotFoundException("Categorya topilmadi!");
		}

		return transactionRepository.findAllByCategoryIdAndUserId(id, user.getId()).stream()
				.map(transactionMapper::toResponse)
				.toList();
	}

	private User getCurrentUser() {
		return (User) SecurityContextHolder
				.getContext()
				.getAuthentication()
				.getPrincipal();
	}
}
