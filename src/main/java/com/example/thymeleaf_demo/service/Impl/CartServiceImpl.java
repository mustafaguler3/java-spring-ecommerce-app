package com.example.thymeleaf_demo.service.Impl;

import com.example.thymeleaf_demo.domain.Cart;
import com.example.thymeleaf_demo.domain.CartItem;
import com.example.thymeleaf_demo.domain.Product;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.CartDto;
import com.example.thymeleaf_demo.dto.CartItemDto;
import com.example.thymeleaf_demo.dto.UserDto;
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

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        Optional<CartItem> existingCartItems = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingCartItems.isPresent()){
            existingCartItems.get().setQuantity(existingCartItems.get().getQuantity() + 1);
        }else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            // add item to cart
            cart.getCartItems().add(cartItem);
        }


        cartRepository.save(cart);
    }
    public Cart findOrCreateCartForUser(User user) {
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        return cart;

    }

    @Override
    public CartDto findByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartDto cartDto = new CartDto();

        List<CartItemDto> cartItemDtos =
                cart.getCartItems()
                        .stream()
                .map(cartItem -> {
                    CartItemDto cartItemDto = new CartItemDto();
                    cartItemDto.setProductName(cartItem.getProduct().getName());
                    cartItemDto.setQuantity(cartItem.getQuantity());
                    cartItemDto.setPrice(df.format(cartItem.getProduct().getPrice()));
                    cartItemDto.setSubtotal(df.format(cartItem.getSubtotal()));
                    cartItemDto.setImageUrl(cartItem.getProduct().getImageUrl());
                    cartItemDto.setDescription(cartItem.getProduct().getDescription());
                    cartItemDto.setBrand(cartItem.getProduct().getBrand());
                    return cartItemDto;
                }).collect(Collectors.toList());

        cartDto.setCartItems(cartItemDtos);
        cartDto.setTotal(df.format(cart.getTotal()));

        return cartDto;
    }

    @Override
    public int getCartItemCountByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        return cart.getCartItems()
                .stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}


















