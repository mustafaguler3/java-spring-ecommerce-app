package com.example.thymeleaf_demo.service;

import com.example.thymeleaf_demo.domain.Product;
import com.example.thymeleaf_demo.domain.Review;
import com.example.thymeleaf_demo.dto.ProductDto;
import com.example.thymeleaf_demo.dto.ReviewDto;
import com.example.thymeleaf_demo.service.Impl.ReviewServiceImpl;

import java.util.List;
import java.util.Map;

public interface ReviewService {
    List<ReviewDto> getAllReviews();
    void saveReview(ReviewDto reviewDto);
    ReviewServiceImpl.ReviewStatistics calculateStatistics(List<ReviewDto> reviews);
    List<ReviewDto> getReviewsByProduct(ProductDto productDto);
    Map<Integer, Long> getRatingCountsByProduct(ProductDto productDto);
}
