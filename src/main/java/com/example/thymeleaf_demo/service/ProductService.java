package com.example.thymeleaf_demo.service;

import com.example.thymeleaf_demo.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> getProducts();
    void createProduct(ProductDto productDto);
    ProductDto getProduct(int id);
}
