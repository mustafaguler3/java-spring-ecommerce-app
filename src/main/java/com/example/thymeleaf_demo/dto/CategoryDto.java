package com.example.thymeleaf_demo.dto;

import com.example.thymeleaf_demo.domain.Product;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CategoryDto {

    private Long id;
    private String name;

    private Set<ProductDto> productDtos;

}
