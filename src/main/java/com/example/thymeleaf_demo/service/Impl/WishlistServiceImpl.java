package com.example.thymeleaf_demo.service.Impl;

import com.example.thymeleaf_demo.domain.Product;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.domain.Wishlist;
import com.example.thymeleaf_demo.dto.ProductDto;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.dto.WishlistDto;
import com.example.thymeleaf_demo.exception.ResourceNotFoundException;
import com.example.thymeleaf_demo.repository.ProductRepository;
import com.example.thymeleaf_demo.repository.UserRepository;
import com.example.thymeleaf_demo.repository.WishlistRepository;
import com.example.thymeleaf_demo.service.ProductService;
import com.example.thymeleaf_demo.service.UserService;
import com.example.thymeleaf_demo.service.WishlistService;
import com.example.thymeleaf_demo.util.DTOConverter;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final Logger logger = LoggerFactory.getLogger(WishlistServiceImpl.class);

    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private DTOConverter dtoConverter;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Wishlist addProductToWishlist(Long userId,Long productId) {
        if (wishlistRepository.existsByUserIdAndProductId(userId,productId)){
            throw new RuntimeException("Product already in wishlist");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(new User(userId));
        wishlist.setProduct(new Product(productId));

        return wishlistRepository.save(wishlist);
        }

    @Override
    public void removeProductFromWishlist(Long userId,Long productId) {


    }

    @Override
    public List<WishlistDto> getWishlistForUser(Long userId) {
       return wishlistRepository.findByUserId(userId)
               .stream()
               .map(wishlist -> {
                   WishlistDto wishlistDto = new WishlistDto();
                   wishlistDto.setImageUrl(wishlist.getProduct().getImageUrl());
                   wishlistDto.setId(wishlistDto.getId());
                   wishlistDto.setUserId(wishlist.getUser().getId());
                   wishlistDto.setProductId(wishlist.getProduct().getId());
                   wishlistDto.setProductName(wishlist.getProduct().getName());
                   wishlistDto.setPrice(wishlist.getProduct().getPrice());
                   wishlistDto.setStock(wishlist.getProduct().getStock());
                   return wishlistDto;
               })
               .collect(Collectors.toList());
    }

    @Override
    public List<WishlistDto> getAllWishlist() {
        return wishlistRepository.findAll()
                .stream()
                .map(dtoConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isProductInWishlist(Long userId, Long productId) {
        return wishlistRepository.existsByUserIdAndProductId(userId, productId);
    }

}






















