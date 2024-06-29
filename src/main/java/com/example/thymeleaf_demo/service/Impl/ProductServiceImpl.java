package com.example.thymeleaf_demo.service.Impl;

import com.example.thymeleaf_demo.domain.Category;
import com.example.thymeleaf_demo.domain.Product;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.CategoryDto;
import com.example.thymeleaf_demo.dto.ProductDto;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.exception.ResourceNotFoundException;
import com.example.thymeleaf_demo.repository.CategoryRepository;
import com.example.thymeleaf_demo.repository.ProductRepository;
import com.example.thymeleaf_demo.service.FileStorageService;
import com.example.thymeleaf_demo.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository, FileStorageService fileStorageService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.fileStorageService = fileStorageService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<ProductDto> getProducts(Pageable pageable) {

        Page<Product> products = productRepository.findAll(pageable);

        if (products == null) {
            throw new ResourceNotFoundException("No products found");
        }

        Page<ProductDto> productDtos =
                products.map(this::convertToDto);

        return productDtos;
    }

    @Override
    public void createProduct(ProductDto productDto) {
        Category category = categoryRepository.findById(productDto.getCategoryId());

        if (category == null) {
            throw new ResourceNotFoundException("Category not found");
        }

        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setStock(product.getStock());
        product.setBrand(productDto.getBrand());
        product.setCategory(category);

        MultipartFile multipartFile = productDto.getImageUrl();

        if (multipartFile != null){
            String fileName = fileStorageService.storeFile(multipartFile,"products");
            product.setImageUrl(fileName);
        }

        productRepository.save(product);

    }

    @Override
    public ProductDto getProduct(Long id) {
        Product product = productRepository.findProductById(id);

        ProductDto productDto = convertToDto(product);

        if (product == null){
            throw new ResourceNotFoundException("Product not found");
        }
        return productDto;
    }

    @Override
    public void updateProduct(ProductDto productDto) {
        Product product = productRepository.findProductById(productDto.getId());

        if (product != null){
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setStock(productDto.getStock());


            if (product.getImageUrl() != null){
                String fileName = fileStorageService.storeFile(productDto.getImageUrl(),"products");
                product.setImageUrl(fileName);
            }
            productRepository.save(product);
        }else {
            throw new ResourceNotFoundException("Product not found");
        }


    }

    private ProductDto convertToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        productDto.setImageUrlShow(product.getImageUrl());
        productDto.setBrand(product.getBrand());
        productDto.setStock(product.getStock());
        productDto.setCategoryId(product.getCategory().getId());

        productDto.setCategory(product.getCategory());

        return productDto;
    }
    private Product convertToEntity(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setImageUrl(productDto.getImageUrlShow());
        product.setBrand(productDto.getBrand());
        product.setStock(productDto.getStock());
         // Set category directly
        return product;
    }
}
