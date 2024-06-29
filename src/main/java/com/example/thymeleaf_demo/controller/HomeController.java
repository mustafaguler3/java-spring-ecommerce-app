package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.Product;
import com.example.thymeleaf_demo.dto.ProductDto;
import com.example.thymeleaf_demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;

    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping(value = {"/","home"})
    public String home(Model model,
                       @RequestParam(name = "page",defaultValue = "0") int pageNumber,
                       @RequestParam(name = "size",defaultValue = "6") int pageSize){

        Page<ProductDto> products =
                productService.getProducts(PageRequest.of(pageNumber, pageSize));

        if (products == null || products.isEmpty()){
            model.addAttribute("error", "No products found");
            return "home";
        }
        model.addAttribute("products",products.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", products.getTotalPages());

        return "home";
    }
}






















