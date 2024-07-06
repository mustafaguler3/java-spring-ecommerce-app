package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShoppingCartController {

    private final UserService userService;

    @Autowired
    public ShoppingCartController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/shopping-cart")
    private String showCart(Model model){

        return "shopping-cart";
    }

}

