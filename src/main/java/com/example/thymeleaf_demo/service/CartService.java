package com.example.thymeleaf_demo.service;

import com.example.thymeleaf_demo.domain.Cart;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.CartDto;
import com.example.thymeleaf_demo.dto.UserDto;

public interface CartService {
    void addProductToCart(Long productId);
    CartDto findByUserId(Long userId);
    int getCartItemCountByUserId(Long userId);
    Cart findOrCreateCartForUser(User user);

}
