package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.*;
import com.example.thymeleaf_demo.dto.*;
import com.example.thymeleaf_demo.service.*;
import com.example.thymeleaf_demo.service.Impl.ReviewServiceImpl;
import com.example.thymeleaf_demo.util.DTOConverter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    private final CartService cartService;

    @Autowired
    public HomeController(ProductService productService, WishlistService wishlistService,
                          ReviewService reviewService,
                          UserService userService, CategoryService categoryService, CartService cartService) {
        this.productService = productService;
        this.wishlistService = wishlistService;
        this.reviewService = reviewService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.cartService = cartService;
    }

    @GetMapping("/b/{productId}")
    public String b(@PathVariable Long productId,
                    Model model,
                    Authentication authentication){
        ProductDto productDto = productService.getProduct(Long.valueOf(productId));
        //Map<Integer,Long> ratingCount = reviewService.getRatingCountsByProduct(productDto);
        List<ReviewDto> reviews = reviewService.getReviewsByProduct(productDto);

        if (productDto == null){
            model.addAttribute("error", "Product not found");
            return "b";
        }
        if (authentication != null && authentication.isAuthenticated()) {
            UserDto currentUser = userService.findByUsername(authentication.getName());
            Long userId = currentUser.getId();

                double averageRating = reviewService.getAverageRating(productDto.getId());
                productDto.setAverageRating(averageRating);
                boolean isWishlist = wishlistService.isProductInWishlist(userId,productDto.getId());
                productDto.setInWishlist(isWishlist);
        } else {
                double averageRating = reviewService.getAverageRating(productDto.getId());
                productDto.setAverageRating(averageRating);
                productDto.setInWishlist(false);
        }
        // Calculate the percentages for each rating
        int[] ratingCounts = new int[5];
        for (ReviewDto review : reviews) {
            ratingCounts[review.getRating() - 1]++;
        }
        double[] ratingPercentages = new double[5];
        for (int i = 0; i < ratingCounts.length; i++) {
            if (reviews.isEmpty()){
                ratingPercentages[i] = 0.0;
            }else {
                ratingPercentages[i] = (double) ratingCounts[i] / reviews.size() * 100;
            }
        }
        model.addAttribute("ratingPercentages", ratingPercentages);
        model.addAttribute("ratingCounts", ratingCounts);
        model.addAttribute("totalReviews", reviews.size());
        model.addAttribute("reviews",reviewService.getReviewsByProduct(productDto));
        model.addAttribute("product", productDto);


        return "b";
    }

    @GetMapping(value = {"/","home"})
    public String home(Model model,
                       @RequestParam(name = "page",defaultValue = "0") int pageNumber,
                       @RequestParam(name = "size",defaultValue = "6") int pageSize,
                       Authentication authentication
                       ){

        Page<ProductDto> products = productService.getProducts(PageRequest.of(pageNumber, pageSize));
        Map<String,Long> categoryProductCounts = categoryService.getCategoryProductCounts();
        // Group by brand and count the occurrences
        Map<String, Long> brandCountMap = products.stream()
                .collect(Collectors.groupingBy(ProductDto::getBrand, Collectors.counting()));


        // Print the results (or add to the model in a web application)
        brandCountMap.forEach((brand, count) -> System.out.println(brand + ": " + count));

        // Add the count map to the model
        model.addAttribute("brandCountMap", brandCountMap);

        model.addAttribute("categoryProductCount", categoryProductCounts);
        model.addAttribute("categories",categoryService.getCategories());

        products.forEach(product -> {
            double averageRating = reviewService.getAverageRating(product.getId());
            product.setAverageRating(averageRating);
        });
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






















