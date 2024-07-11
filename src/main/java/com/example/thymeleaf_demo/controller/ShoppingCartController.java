package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.Basket;
import com.example.thymeleaf_demo.domain.UserDetailsImpl;
import com.example.thymeleaf_demo.dto.BasketDto;
import com.example.thymeleaf_demo.service.CartService;
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
@SessionAttributes("cart")
public class ShoppingCartController {

    private final UserService userService;
    private final CartService cartService;

    @Autowired
    public ShoppingCartController(UserService userService, CartService cartService) {
        this.userService = userService;
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    private String showCart(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getUser().getId();

        BasketDto basketDto = cartService.findByUserId(userId);

        if (basketDto.getCartItems().isEmpty()){
            model.addAttribute("message","Your cart is empty");
            return "shopping-cart";
        }

        model.addAttribute("cart", basketDto);


        return "shopping-cart";
    }
    @ModelAttribute("cart")
    public Basket getCart() {
        return new Basket(); // Varsayılan olarak boş bir sepet döndürülebilir
    }

    @PostMapping("/cart/add/{productId}")
    public String  addToCart(
                            @PathVariable Long productId,
                            Model model){

        cartService.addProductToCart(productId);
        model.addAttribute("message", "Ürün sepete eklendi");

        // Kullanıcıyı ana sayfaya yönlendir
        return "redirect:/home";
    }

}

