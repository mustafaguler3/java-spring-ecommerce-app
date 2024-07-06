package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.Cart;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.domain.UserDetailsImpl;
import com.example.thymeleaf_demo.dto.CartDto;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.service.CartService;
import com.example.thymeleaf_demo.service.UserService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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

        CartDto cartDto = cartService.findByUserId(userId);

        if (cartDto.getCartItems().isEmpty()){
            model.addAttribute("message","Your cart is empty");
            return "shopping-cart";
        }

        model.addAttribute("cart",cartDto);


        return "shopping-cart";
    }
    @ModelAttribute("cart")
    public Cart getCart() {
        return new Cart(); // Varsayılan olarak boş bir sepet döndürülebilir
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

