package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProductService productService;

    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping(value = {"/","home"})
    public String home(Model model){
        model.addAttribute("products",productService.getProducts());

        return "home";
    }
}






















