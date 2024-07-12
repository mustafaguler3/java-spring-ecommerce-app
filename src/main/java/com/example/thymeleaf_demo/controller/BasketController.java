package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.Basket;
import com.example.thymeleaf_demo.domain.UserDetailsImpl;
import com.example.thymeleaf_demo.dto.BasketDto;
import com.example.thymeleaf_demo.service.BasketService;
import com.example.thymeleaf_demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class BasketController {

    private final UserService userService;
    private final BasketService basketService;

    @Autowired
    public BasketController(UserService userService, BasketService basketService) {
        this.userService = userService;
        this.basketService = basketService;
    }

    @GetMapping("/basket")
    private String showCart(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getUser().getId();

        BasketDto basketDto = basketService.findByUserId(userId);

        if (basketDto.getBasketItems().isEmpty()){
            model.addAttribute("message","Your cart is empty");
            return "shopping-cart";
        }

        model.addAttribute("total", basketDto.getTotal());
        model.addAttribute("basket", basketDto.getBasketItems());


        return "shopping-cart";
    }

    @ModelAttribute("cart")
    public Basket getCart() {
        return new Basket(); // Varsayılan olarak boş bir sepet döndürülebilir
    }

    @PostMapping("/basket/add/{productId}")
    public String  addToCart(
                            @PathVariable Long productId,
                            Model model){

        basketService.addProductToBasket(productId);
        model.addAttribute("message", "Ürün sepete eklendi");

        // Kullanıcıyı ana sayfaya yönlendir
        return "redirect:/home";
    }

}

