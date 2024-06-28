package com.example.thymeleaf_demo.service.Impl;

import com.example.thymeleaf_demo.domain.Category;
import com.example.thymeleaf_demo.domain.Product;
import com.example.thymeleaf_demo.dto.CategoryDto;
import com.example.thymeleaf_demo.dto.ProductDto;
import com.example.thymeleaf_demo.repository.CategoryRepository;
import com.example.thymeleaf_demo.repository.ProductRepository;
import com.example.thymeleaf_demo.service.FileStorageService;
import com.example.thymeleaf_demo.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public List<ProductDto> getProducts() {

        List<Product> products = productRepository.findAll();

        if (products == null){
            throw new RuntimeException("Products not found");
        }

        return products.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void createProduct(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setDescription(productDto.getDescription());
        product.setStock(product.getStock());

        Optional<Category> category =
                categoryRepository.findById(productDto.getCategoryId());

        if (category.isPresent()){
            product.setCategory(category.get());
        }else {
            throw new RuntimeException("Category not found");
        }

        if (productDto.getImageUrlShow() != null){
            product.setImageUrl(productDto.getImageUrlShow());
        }
        MultipartFile multipartFile = productDto.getImageUrl();

        if (multipartFile != null){
            String fileName = fileStorageService.storeFile(multipartFile,"products");
            product.setImageUrl(fileName);
        }

        productRepository.save(product);

    }

    @Override
    public ProductDto getProduct(int id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()){
            return product.stream().map(this::convertToDto).findFirst().get();
        }
        return null;
    }

    private ProductDto convertToDto(Product product){
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        productDto.setImageUrlShow(product.getImageUrl());
        productDto.setQuantity(product.getQuantity());
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setCategory(product.getCategory());
        productDto.setStock(product.getStock());

        return productDto;
    }

    private Product convertToEntity(ProductDto productDto){
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setImageUrl(productDto.getImageUrlShow());
        product.setQuantity(productDto.getQuantity());
        product.setCategory(productDto.getCategory());
        product.setStock(productDto.getStock());
        return product;
    }
}
