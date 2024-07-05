package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.Product;
import com.example.thymeleaf_demo.domain.Review;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.ProductDto;
import com.example.thymeleaf_demo.dto.ReviewDto;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.service.*;
import com.example.thymeleaf_demo.util.DTOConverter;
import groovy.util.logging.Log4j2;
import groovy.util.logging.Slf4j;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.Fuseable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


@Log4j2
@Controller
public class ProductsController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final FileStorageService fileStorageService;
    private final ReviewService reviewService;
    private final UserService userService;
    private final DTOConverter dtoConverter;
    private final WishlistService wishlistService;

    @Autowired
    public ProductsController(ProductService productService, CategoryService categoryService,
                              FileStorageService fileStorageService,
                              ReviewService reviewService, UserService userService,
                              DTOConverter dtoConverter, WishlistService wishlistService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.fileStorageService = fileStorageService;
        this.reviewService = reviewService;
        this.userService = userService;
        this.dtoConverter = dtoConverter;
        this.wishlistService = wishlistService;
    }

    @GetMapping("/products/{productId}")
    public String getProduct(@PathVariable("productId") Integer productId,
                             Model model){

        ProductDto productDto = productService.getProduct(Long.valueOf(productId));
        //Map<Integer,Long> ratingCount = reviewService.getRatingCountsByProduct(productDto);
        List<ReviewDto> reviews = reviewService.getReviewsByProduct(productDto);

        if (productDto == null){
            model.addAttribute("error", "Product not found");
            return "product-detail";
        }
        // Calculate the percentages for each rating
        int[] ratingCounts = new int[5];
        for (ReviewDto review : reviews) {
            ratingCounts[review.getRating() - 1]++;
        }
        double[] ratingPercentages = new double[5];
        for (int i = 0; i < ratingCounts.length; i++) {
            if (reviews.size() == 0){
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


        return "product-detail";
    }

    @PostMapping("/products/review")
    public String addReview(
                               Model model,
                               @RequestParam("id") Long productId,
                               @RequestParam("rating") int rating,
                               @RequestParam("comment") String comment,
                               @RequestParam(required = false,name = "guestName") String guestName
                               ) {

        ProductDto productDto = productService.getProduct(productId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usernameCurrent = authentication.getName();
        UserDto userDto = userService.findByUsername(usernameCurrent);


        if (userDto != null){
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setComment(comment);
            reviewDto.setRating(rating);
            reviewDto.setProductDto(productDto);
            reviewDto.setPublishedDate(LocalDateTime.now());
            reviewDto.setUserDto(userDto);

            reviewService.saveReview(reviewDto);

            model.addAttribute("success", "Review successfully submitted");

            return "redirect:/products/"+productId;
        }
        return "redirect:/login";
        }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/products")
    public String getProducts(Model model,
                              @RequestParam(defaultValue = "0",name = "page") int pageNumber,
                              @RequestParam(defaultValue = "4",name = "size") int pageSize){

        Page<ProductDto> products = productService.getProducts(PageRequest.of(pageNumber,pageSize));

        if (products == null){
            model.addAttribute("error", "No products found");
            return "product-list";
        }

        model.addAttribute("products",products.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("categories",categoryService.getCategories());

        return "product-list";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/products/new")
    public String getProductForm(Model model){
        model.addAttribute("productDto",new ProductDto());
        model.addAttribute("categories",categoryService.getCategories());
        return "create-product";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/products/new")
    public String addProduct(@ModelAttribute("productDto") @Valid ProductDto productDto,
                             @RequestParam("file") MultipartFile multipartFile,
                             BindingResult bindingResult,
                             Model model){

        if (bindingResult.hasErrors()){
            model.addAttribute("categories",categoryService.getCategories());
            return "create-product";
        }

        if (multipartFile != null || !multipartFile.isEmpty()){
            productDto.setImageUrl(multipartFile);
        }

        productService.createProduct(productDto);
        model.addAttribute("success","Product created successfully");

        return "redirect:/products";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/products/edit/{id}")
    public String showUpdateProduct(@PathVariable("id") Long id,
                                    Model model){
        ProductDto productDto = productService.getProduct(id);

        if (productDto == null){
            model.addAttribute("error", "Product not found");
            return "product-edit";
        }
        model.addAttribute("productDto", productDto);
        model.addAttribute("categories",categoryService.getCategories());

        return "product-edit";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/products/edit")
    public String showUpdateProduct(@ModelAttribute("productDto") ProductDto productDto,
                                    @RequestParam("file") MultipartFile multipartFile,
                                    Model model,
                                    BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "product-edit";
        }

        if (multipartFile != null){
            productDto.setImageUrl(multipartFile);
        }
        model.addAttribute("success","Product updated successfully");
        productService.updateProduct(productDto);

        return "product-edit";
    }


}














