package com.example.personal_finance_api.mapper;

import org.springframework.stereotype.Component;

import com.example.personal_finance_api.dto.CategoryRes;
import com.example.personal_finance_api.entity.Category;

@Component
public class CategoryMapper {
  public CategoryRes toResponse(Category category) {
    return new CategoryRes(
        category.getId(),
        category.getName(),
        category.getType(),
        category.getCreatedAt(),
        category.getUpdatedAt());
  }
}
