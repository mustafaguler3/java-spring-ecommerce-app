package com.example.thymeleaf_demo.service.Impl;

import com.example.thymeleaf_demo.domain.Basket;
import com.example.thymeleaf_demo.domain.BasketItem;
import com.example.thymeleaf_demo.domain.Product;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.BasketDto;
import com.example.thymeleaf_demo.dto.BasketItemDto;
import com.example.thymeleaf_demo.exception.ResourceNotFoundException;
import com.example.thymeleaf_demo.repository.CartRepository;
import com.example.thymeleaf_demo.repository.ProductRepository;
import com.example.thymeleaf_demo.repository.UserRepository;
import com.example.thymeleaf_demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public void addProductToCart(Long productId) {

        // retrieve product
        Product product =
                productRepository.findById(Math.toIntExact(productId))
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();

        // retrieve user
        User user = userRepository.findByUsername(currentUser);

        Basket basket = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Basket newBasket = new Basket();
                    newBasket.setUser(user);
                    return cartRepository.save(newBasket);
                });

        Optional<BasketItem> existingCartItems = basket.getBasketItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingCartItems.isPresent()){
            existingCartItems.get().setQuantity(existingCartItems.get().getQuantity() + 1);
        }else {
            BasketItem basketItem = new BasketItem();
            basketItem.setBasket(basket);
            basketItem.setProduct(product);
            basketItem.setQuantity(1);
            // add item to cart
            basket.getBasketItems().add(basketItem);
        }


        cartRepository.save(basket);
    }
    public Basket findOrCreateCartForUser(User user) {
        Basket basket = cartRepository.findByUser(user);
        if (basket == null) {
            basket = new Basket();
            basket.setUser(user);
            cartRepository.save(basket);
        }
        return basket;

    }

    @Override
    public BasketDto findByUserId(Long userId) {
        Basket basket = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        BasketDto basketDto = new BasketDto();

        List<BasketItemDto> basketItemDtos =
                basket.getBasketItems()
                        .stream()
                .map(basketItem -> {
                    BasketItemDto basketItemDto = new BasketItemDto();
                    basketItemDto.setProductId(basketItem.getProduct().getId());
                    basketItemDto.setProductName(basketItem.getProduct().getName());
                    basketItemDto.setQuantity(basketItem.getQuantity());
                    basketItemDto.setPrice(Double.parseDouble(df.format(basketItem.getProduct().getPrice())));
                    basketItemDto.setSubtotal(df.format(basketItem.getSubtotal()));
                    basketItemDto.setImageUrl(basketItem.getProduct().getImageUrl());
                    basketItemDto.setDescription(basketItem.getProduct().getDescription());
                    basketItemDto.setBrand(basketItem.getProduct().getBrand());
                    return basketItemDto;
                }).collect(Collectors.toList());

        basketDto.setCartItems(basketItemDtos);
        basketDto.setTotal(df.format(basket.getTotal()));

        return basketDto;
    }

    @Override
    public int getCartItemCountByUserId(Long userId) {
        Basket basket = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        return basket.getBasketItems()
                .stream()
                .mapToInt(BasketItem::getQuantity)
                .sum();
    }
}


















