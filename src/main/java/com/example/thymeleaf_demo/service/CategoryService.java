package com.example.thymeleaf_demo.service;

import com.example.thymeleaf_demo.domain.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    List<Category> getCategories();
    Map<String, Long> getCategoryProductCounts();
    List<Category> getCategoriesWithProduct();
}
