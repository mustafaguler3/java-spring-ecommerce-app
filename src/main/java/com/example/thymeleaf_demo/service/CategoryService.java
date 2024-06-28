package com.example.thymeleaf_demo.service;

import com.example.thymeleaf_demo.domain.Category;
import com.example.thymeleaf_demo.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getCategories();
}
