package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.Product;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.domain.UserDetailsImpl;
import com.example.thymeleaf_demo.domain.Wishlist;
import com.example.thymeleaf_demo.dto.ProductDto;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.dto.WishlistDto;
import com.example.thymeleaf_demo.repository.UserRepository;
import com.example.thymeleaf_demo.service.ProductService;
import com.example.thymeleaf_demo.service.UserService;
import com.example.thymeleaf_demo.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class WishlistController {

    private final WishlistService wishlistService;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public WishlistController(WishlistService wishlistService, ProductService productService, UserService userService) {
        this.wishlistService = wishlistService;
        this.productService = productService;
        this.userService = userService;
    }


    @PostMapping("/wishlist/add")
    public String addProductToWishlist(
            @RequestParam Long productId, Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Long userId = userDetails.getUser().getId();// Assuming UserDetails has a method to get user ID

            Wishlist wishlist = wishlistService.addProductToWishlist(userId, productId);
            model.addAttribute("message", "Product added to wishlist successfully");
            return "home";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "home"; // Redirect to products page or wherever appropriate
        }
    }

    @PostMapping("/wishlist/remove/{productId}")
    @ResponseBody
    public ResponseEntity<?> removeProductFromWishlist(@PathVariable("productId") Long productId,
                                                            Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("message", "User not authenticated")
            );
        }
        UserDto userDto = userService.findByUsername(authentication.getName());
        wishlistService.removeProductFromWishlist(userDto.getId(), productId);

        return ResponseEntity.ok(Map.of("message", "Product removed from wishlist"));
    }

    @GetMapping("/wishlist")
    public String showUserWi(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            model.addAttribute("error", "User not authenticated");
            return "wishlist";
        }

        UserDto userDto = userService.findByUsername(authentication.getName());
        List<WishlistDto> wishlist = wishlistService.getWishlistForUser(userDto.getId());

        if (wishlist == null || wishlist.isEmpty()) {
            model.addAttribute("error", "Your wishlist is empty");
        } else {
            model.addAttribute("wishlist", wishlist);
        }

        return "wishlist";
    }
}






















