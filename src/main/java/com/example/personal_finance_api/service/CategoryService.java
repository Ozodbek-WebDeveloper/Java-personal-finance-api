package com.example.personal_finance_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.example.personal_finance_api.exception.BadRequestException;
import com.example.personal_finance_api.exception.ResourceNotFoundException;
import com.example.personal_finance_api.mapper.CategoryMapper;
import com.example.personal_finance_api.dto.CategoryReq;
import com.example.personal_finance_api.dto.CategoryRes;
import com.example.personal_finance_api.entity.Category;
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

		if (categoryRepository.findByNameIgnoreCase(name).isPresent()) {
			throw new BadRequestException("Bu nomdagi kategoriya allaqachon mavjud");
		}

		Category category = new Category(name, categoryReq.type());

		Category newCategory = categoryRepository.save(category);
		return categoryMapper.toResponse(newCategory);
	}

	public List<CategoryRes> getAllCategory() {
		return categoryRepository.findAll().stream()
				.map(categoryMapper::toResponse)
				.toList();
	}

	public List<CategoryRes> getAllCategoryByType(TransactionType type) {
		return categoryRepository.findAllByType(type).stream().map(categoryMapper::toResponse)
				.toList();
	}

	public CategoryRes findOne(Integer id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category topilmadi: " + id));
		return categoryMapper.toResponse(category);
	}

	public CategoryRes update(Integer id, CategoryReq categoryReq) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category topilmadi: " + id));

		categoryRepository.findByNameIgnoreCase(categoryReq.name().trim())
				.filter(c -> !c.getId().equals(id))
				.ifPresent(c -> {
					throw new BadRequestException("Bu nomdagi kategoriya allaqachon mavjud!");
				});

		category.setName(categoryReq.name());
		return categoryMapper.toResponse(categoryRepository.save(category));
	}

	public String delete(Integer id) {
		categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Categorya topilmadi!"));

		if (transactionRepository.existsByCategoryId(id)) {
			throw new BadRequestException("Categoryaga  tegishli transactionlar mavjud!");
		}

		categoryRepository.deleteById(id);
		return "Categorya muvofaqiyarli o'chirildi.";
	}

}
