package com.example.thymeleaf_demo.util;

import com.example.thymeleaf_demo.domain.Product;
import com.example.thymeleaf_demo.domain.Review;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.ProductDto;
import com.example.thymeleaf_demo.dto.ReviewDto;
import com.example.thymeleaf_demo.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DTOConverter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DTOConverter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Product convertToProduct(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setStock(productDto.getStock());
        product.setBrand(productDto.getBrand());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());

        List<Review> reviews = productDto.getReviews()
                        .stream()
                                .map(review -> convertToReview(review))
                                        .toList();

        product.setReviews(reviews);

        return product;
    }

    public ProductDto convertToProductDTO(Product product) {
        ProductDto productDTO = new ProductDto();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setBrand(product.getBrand());
        productDTO.setImageUrlShow(product.getImageUrl());
        productDTO.setStock(product.getStock());

        List<ReviewDto> reviewDTOs =
                product.getReviews().stream()
                .map(this::convertToReviewDTO)
                .collect(Collectors.toList());
        productDTO.setReviews(reviewDTOs);
        return productDTO;
    }

    public ReviewDto convertToReviewDTO(Review review) {
        ReviewDto reviewDTO = new ReviewDto();
        reviewDTO.setReviewId(review.getReviewId());
        reviewDTO.setUsername(review.getUser().getUsername());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setComment(review.getComment());
        reviewDTO.setProfilePicture(review.getUser().getProfilePicture());

        if (review.getPublishedDate() != null){
            //reviewDTO.setPublishedDate(review.getPublishedDate()
                    //.format(DateTimeFormatter.ofPattern("MMMM ,dd, yyyy")));
            reviewDTO.setPublishedDate(review.getPublishedDate());
        }
        return reviewDTO;
    }

    public Review convertToReview(ReviewDto reviewDto){
        Review review = new Review();
        review.setReviewId(reviewDto.getReviewId());
        review.setRating(reviewDto.getRating());
        review.setComment(reviewDto.getComment());
        review.setPublishedDate(LocalDateTime.now());

        review.setUser(convertToEntity(reviewDto.getUserDto()));
        review.setProduct(convertToProduct(reviewDto.getProductDto()));

        return review;
    }


    public UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setDescription(user.getDescription());
        userDto.setIsEnabled(user.getIsEnabled()); // Convert Boolean field
        userDto.setProfilePictureUrl(user.getProfilePicture()); // Set the profile picture URL
        return userDto;
    }

    public User convertToEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setDescription(userDto.getDescription());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setIsEnabled(userDto.getIsEnabled());
        user.setProfilePicture(userDto.getProfilePictureUrl()); // Set the profile picture URL
        return user;
    }
}


















