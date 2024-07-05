package com.example.thymeleaf_demo.service;

import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.domain.Wishlist;
import com.example.thymeleaf_demo.dto.ProductDto;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.dto.WishlistDto;

import java.util.List;
import java.util.Set;

public interface WishlistService {
    Wishlist addProductToWishlist(Long userId,Long productId);
    void removeProductFromWishlist(Long userId,Long productId);
    List<WishlistDto> getWishlistForUser(Long userId);
    boolean isProductInWishlist(Long userId, Long productId);
}
