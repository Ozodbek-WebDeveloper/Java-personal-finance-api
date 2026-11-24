package com.example.personal_finance_api.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.personal_finance_api.dto.CategoryReq;
import com.example.personal_finance_api.dto.CategoryRes;
import com.example.personal_finance_api.enums.TransactionType;
import com.example.personal_finance_api.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/category")
    public CategoryRes create(@Valid @RequestBody CategoryReq categoryReq) {
        return categoryService.create(categoryReq);
    }

    @GetMapping("/category")
    public List<CategoryRes> getAllCategory(@RequestParam(required = false) TransactionType type) {
        if (type == null) {
            return categoryService.getAllCategory();
        }
        return categoryService.getAllCategoryByType(type);
    }

    @GetMapping("/category/{id}")
    public CategoryRes findOne(@PathVariable Integer id) {
        return categoryService.findOne(id);
    }

    @PutMapping("/category/{id}")
    public CategoryRes update(@PathVariable Integer id, @Valid   @RequestBody CategoryReq categoryReq) {
        return categoryService.update(id, categoryReq);
    }

    @DeleteMapping("/category/{id}")
    public  String delete(@PathVariable  Integer id){
        return categoryService.delete(id);
    }
}
