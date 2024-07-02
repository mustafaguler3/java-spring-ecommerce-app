package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.Product;
import com.example.thymeleaf_demo.domain.Review;
import com.example.thymeleaf_demo.dto.ProductDto;
import com.example.thymeleaf_demo.dto.ReviewDto;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.service.Impl.ReviewServiceImpl;
import com.example.thymeleaf_demo.service.ProductService;
import com.example.thymeleaf_demo.service.ReviewService;
import com.example.thymeleaf_demo.service.UserService;
import com.example.thymeleaf_demo.util.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final ProductService productService;
    private final ReviewService reviewService;
    private final UserService userService;

    @Autowired
    public HomeController(ProductService productService,
                          ReviewService reviewService,
                          UserService userService) {
        this.productService = productService;
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping("/a")
    public String productDetails(Model model){
        List<ReviewDto> reviews = reviewService.getAllReviews();

        ReviewServiceImpl.ReviewStatistics stats = reviewService.calculateStatistics(reviews);

        model.addAttribute("totalReviews", stats.getTotalReviews());
        model.addAttribute("averageRating", stats.getAverageRating());
        model.addAttribute("percent5Star", stats.getPercent5Star());
        model.addAttribute("percent4Star", stats.getPercent4Star());
        model.addAttribute("percent3Star", stats.getPercent3Star());
        model.addAttribute("percent2Star", stats.getPercent2Star());
        model.addAttribute("percent1Star", stats.getPercent1Star());
        model.addAttribute("reviews", reviews);

        return "a";
    }

    @GetMapping(value = {"/","home"})
    public String home(Model model,
                       @RequestParam(name = "page",defaultValue = "0") int pageNumber,
                       @RequestParam(name = "size",defaultValue = "6") int pageSize
                       ){

        Page<ProductDto> products =
                productService.getProducts(PageRequest.of(pageNumber, pageSize));

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






















