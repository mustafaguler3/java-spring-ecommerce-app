package com.example.thymeleaf_demo.service;

import com.example.thymeleaf_demo.domain.Basket;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.BasketDto;

public interface BasketService {
    void addProductToBasket(Long productId);
    BasketDto findByUserId(Long userId);
    int getBasketItemCountByUserId(Long userId);
    Basket findOrCreateBasketForUser(User user);
    void deleteBasketByBasketId(Long basketId);
}
