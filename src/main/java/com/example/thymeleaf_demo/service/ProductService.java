package com.example.thymeleaf_demo.service;

import com.example.thymeleaf_demo.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Page<ProductDto> getProducts(Pageable pageable);
    void createProduct(ProductDto productDto);
    ProductDto getProduct(Long id);
    void updateProduct(ProductDto productDto);
}
