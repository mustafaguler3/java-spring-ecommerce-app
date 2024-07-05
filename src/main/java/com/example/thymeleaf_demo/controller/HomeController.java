package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.Product;
import com.example.thymeleaf_demo.domain.Review;
import com.example.thymeleaf_demo.domain.UserDetailsImpl;
import com.example.thymeleaf_demo.domain.Wishlist;
import com.example.thymeleaf_demo.dto.ProductDto;
import com.example.thymeleaf_demo.dto.ReviewDto;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.dto.WishlistDto;
import com.example.thymeleaf_demo.service.*;
import com.example.thymeleaf_demo.service.Impl.ReviewServiceImpl;
import com.example.thymeleaf_demo.util.DTOConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class HomeController {

    private final ProductService productService;
    private final WishlistService wishlistService;
    private final ReviewService reviewService;
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public HomeController(ProductService productService, WishlistService wishlistService,
                          ReviewService reviewService,
                          UserService userService, CategoryService categoryService) {
        this.productService = productService;
        this.wishlistService = wishlistService;
        this.reviewService = reviewService;
        this.userService = userService;
        this.categoryService = categoryService;
    }


    @GetMapping(value = {"/","home"})
    public String home(Model model,
                       @RequestParam(name = "page",defaultValue = "0") int pageNumber,
                       @RequestParam(name = "size",defaultValue = "6") int pageSize,
                       Authentication authentication
                       ){

        Page<ProductDto> products = productService.getProducts(PageRequest.of(pageNumber, pageSize));

        model.addAttribute("categories",categoryService.getCategories());

        if (authentication != null && authentication.isAuthenticated()) {
            UserDto currentUser = userService.findByUsername(authentication.getName());
            Long userId = currentUser.getId();
            products.forEach(product -> {
                double averageRating = reviewService.getAverageRating(product.getId());
                product.setAverageRating(averageRating);
                boolean isWishlist = wishlistService.isProductInWishlist(userId,product.getId());
                product.setInWishlist(isWishlist);
            });
        } else {
            products.forEach(product -> {
                double averageRating = reviewService.getAverageRating(product.getId());
                product.setAverageRating(averageRating);
                product.setInWishlist(false);  // Kullanıcı giriş yapmamışsa wishlist kontrolü yapılmaz
            });
        }

        if (products == null || products.isEmpty()){
            model.addAttribute("error", "No products found");
            return "home";
        }

        model.addAttribute("products",products.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", products.getTotalPages());

        return "home";
    }
}






















