package com.example.thymeleaf_demo.service.Impl;

import com.example.thymeleaf_demo.domain.Category;
import com.example.thymeleaf_demo.dto.CategoryDto;
import com.example.thymeleaf_demo.repository.CategoryRepository;
import com.example.thymeleaf_demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDto> getCategories() {
        List<Category> categories = categoryRepository.findAll();

        if (categories != null) {
            return categories.stream()
                   .map(this::convertToDto)
                   .toList();
        }

        return null;
    }

    private CategoryDto convertToDto(Category category){

        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setProducts(category.getProducts());

        return dto;
    }
}
