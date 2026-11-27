package com.example.personal_finance_api.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.personal_finance_api.exception.BadRequestException;
import com.example.personal_finance_api.exception.ResourceNotFoundException;
import com.example.personal_finance_api.mapper.CategoryMapper;
import com.example.personal_finance_api.dto.CategoryReq;
import com.example.personal_finance_api.dto.CategoryRes;
import com.example.personal_finance_api.entity.Category;
import com.example.personal_finance_api.entity.User;
import com.example.personal_finance_api.enums.TransactionType;
import com.example.personal_finance_api.repository.CategoryRepository;
import com.example.personal_finance_api.repository.TransactionRepository;

@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;
	private final TransactionRepository transactionRepository;

	public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper,
			TransactionRepository transactionRepository) {
		this.categoryRepository = categoryRepository;
		this.categoryMapper = categoryMapper;
		this.transactionRepository = transactionRepository;
	}

	public CategoryRes create(CategoryReq categoryReq) {
		String name = categoryReq.name().trim();
		User user = getCurrentUser();
		if (categoryRepository.findByNameIgnoreCase(name, user.getId()).isPresent()) {
			throw new BadRequestException("Bu nomdagi kategoriya allaqachon mavjud");
		}

		Category category = new Category(name, categoryReq.type(), user);

		Category newCategory = categoryRepository.save(category);
		return categoryMapper.toResponse(newCategory);
	}

	public List<CategoryRes> getAllCategory() {
		User user = getCurrentUser();
		return categoryRepository.findAllByUserId(user.getId()).stream()
				.map(categoryMapper::toResponse)
				.toList();
	}

	public List<CategoryRes> getAllCategoryByType(TransactionType type) {
		User user = getCurrentUser();
		return categoryRepository.findAllByType(type, user.getId()).stream().map(categoryMapper::toResponse)
				.toList();
	}

	public CategoryRes findOne(Integer id) {
		User user = getCurrentUser();
		Category category = categoryRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Category topilmadi: " + id));
		return categoryMapper.toResponse(category);
	}

	public CategoryRes update(Integer id, CategoryReq categoryReq) {
		User user = getCurrentUser();
		Category category = categoryRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Category topilmadi: " + id));

		categoryRepository.findByNameIgnoreCase(categoryReq.name().trim(), user.getId())
				.filter(c -> !c.getId().equals(id))
				.ifPresent(c -> {
					throw new BadRequestException("Bu nomdagi kategoriya allaqachon mavjud!");
				});

		category.setName(categoryReq.name());
		return categoryMapper.toResponse(categoryRepository.save(category));
	}

	public String delete(Integer id) {
		User user = getCurrentUser();
		Category category = categoryRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Categorya topilmadi!"));

		if (transactionRepository.existsByCategoryId(id)) {
			throw new BadRequestException("Categoryaga  tegishli transactionlar mavjud!");
		}

		categoryRepository.delete(category);
		return "Categorya muvofaqiyarli o'chirildi.";
	}

	private User getCurrentUser() {
		return (User) SecurityContextHolder
				.getContext()
				.getAuthentication()
				.getPrincipal();
	}

}
