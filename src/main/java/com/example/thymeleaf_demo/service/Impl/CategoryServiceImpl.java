package com.example.thymeleaf_demo.service.Impl;

import com.example.thymeleaf_demo.domain.Category;
import com.example.thymeleaf_demo.repository.CategoryRepository;
import com.example.thymeleaf_demo.service.CategoryService;
import com.example.thymeleaf_demo.util.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final DTOConverter dtoConverter;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, DTOConverter dtoConverter) {
        this.categoryRepository = categoryRepository;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public List<Category> getCategories() {
        List<Category> results = categoryRepository.findAll();
        return results;
    }

    @Override
    public Map<String, Long> getCategoryProductCounts() {
        List<Category> categories = categoryRepository.findAll();
        Map<String,Long> categoryProductCounts = new HashMap<>();

        for (Category category : categories) {
            long count = category.getProducts().size();
            categoryProductCounts.put(category.getName(),count);
        }

        return categoryProductCounts;
    }

    @Override
    public List<Category> getCategoriesWithProduct() {
        List<Category> results = categoryRepository.findAllWithProducts();
        return results;
    }


}
