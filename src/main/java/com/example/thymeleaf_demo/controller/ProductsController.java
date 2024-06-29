package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.dto.ProductDto;
import com.example.thymeleaf_demo.service.CategoryService;
import com.example.thymeleaf_demo.service.FileStorageService;
import com.example.thymeleaf_demo.service.ProductService;
import groovy.util.logging.Log4j2;
import groovy.util.logging.Slf4j;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.Fuseable;

import java.util.List;

@Slf4j
@Controller
public class ProductsController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final FileStorageService fileStorageService;

    @Autowired
    public ProductsController(ProductService productService, CategoryService categoryService, FileStorageService fileStorageService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/products/{id}")
    public String getProduct(@PathVariable("id") Long id,
                             Model model){
        ProductDto productDto = productService.getProduct(id);

        if (productDto == null){
            model.addAttribute("error", "Product not found");
            return "product-detail";
        }

        model.addAttribute("product", productDto);
        return "product-detail";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/products")
    public String getProducts(Model model,
                              @RequestParam(defaultValue = "0",name = "page") int pageNumber,
                              @RequestParam(defaultValue = "4",name = "size") int pageSize){

        Page<ProductDto> products = productService.getProducts(PageRequest.of(pageNumber,pageSize));

        if (products == null){
            model.addAttribute("error", "No products found");
            return "product-list";
        }

        model.addAttribute("products",products.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("categories",categoryService.getCategories());

        return "product-list";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/products/new")
    public String getProductForm(Model model){
        model.addAttribute("productDto",new ProductDto());
        model.addAttribute("categories",categoryService.getCategories());
        return "create-product";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/products/new")
    public String addProduct(@ModelAttribute("productDto") @Valid ProductDto productDto,
                             @RequestParam("file") MultipartFile multipartFile,
                             BindingResult bindingResult,
                             Model model){

        if (bindingResult.hasErrors()){
            model.addAttribute("categories",categoryService.getCategories());
            return "create-product";
        }

        if (multipartFile != null || !multipartFile.isEmpty()){
            productDto.setImageUrl(multipartFile);
        }

        productService.createProduct(productDto);
        model.addAttribute("success","Product created successfully");

        return "redirect:/products";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/products/edit/{id}")
    public String showUpdateProduct(@PathVariable("id") Long id,
                                    Model model){
        ProductDto productDto = productService.getProduct(id);

        if (productDto == null){
            model.addAttribute("error", "Product not found");
            return "product-edit";
        }
        model.addAttribute("productDto", productDto);
        model.addAttribute("categories",categoryService.getCategories());

        return "product-edit";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/products/edit")
    public String showUpdateProduct(@ModelAttribute("productDto") ProductDto productDto,
                                    @RequestParam("file") MultipartFile multipartFile,
                                    Model model,
                                    BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "product-edit";
        }

        if (multipartFile != null){
            productDto.setImageUrl(multipartFile);
        }
        model.addAttribute("success","Product updated successfully");
        productService.updateProduct(productDto);

        return "product-edit";
    }

}














