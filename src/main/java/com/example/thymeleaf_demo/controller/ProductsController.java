package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.dto.ProductDto;
import com.example.thymeleaf_demo.service.CategoryService;
import com.example.thymeleaf_demo.service.FileStorageService;
import com.example.thymeleaf_demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.Fuseable;

import java.util.List;

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
    public String getProduct(@PathVariable("id") int id,
                             Model model){
        ProductDto productDto = productService.getProduct(id);
        if (productDto == null){
            model.addAttribute("error", "Product not found");
            return "product-detail";
        }
        model.addAttribute("product", productDto);
        return "product-detail";
    }

    @GetMapping("/products")
    public String getProducts(Model model){
        List<ProductDto> products = productService.getProducts();

        if (products == null || products.stream().count() == 0){
            model.addAttribute("error","There are no products");
            return "product-list";
        }

        model.addAttribute("products",productService.getProducts());
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
                             BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "create-product";
        }

        if (multipartFile != null){
            productDto.setImageUrl(multipartFile);
        }

        productService.createProduct(productDto);

        return "redirect:/products";
    }
}
