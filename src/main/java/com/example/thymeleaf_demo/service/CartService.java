package com.example.thymeleaf_demo.service;

import com.example.thymeleaf_demo.domain.Basket;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.BasketDto;

public interface CartService {
    void addProductToCart(Long productId);
    BasketDto findByUserId(Long userId);
    int getCartItemCountByUserId(Long userId);
    Basket findOrCreateCartForUser(User user);

}
