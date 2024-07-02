package com.example.thymeleaf_demo.service.Impl;

import com.example.thymeleaf_demo.domain.Product;
import com.example.thymeleaf_demo.domain.Review;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.ProductDto;
import com.example.thymeleaf_demo.dto.ReviewDto;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.repository.ProductRepository;
import com.example.thymeleaf_demo.repository.ReviewRepository;
import com.example.thymeleaf_demo.repository.UserRepository;
import com.example.thymeleaf_demo.service.ReviewService;
import com.example.thymeleaf_demo.util.DTOConverter;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DTOConverter dtoConverter;

    public List<ReviewDto> getReviewsByProduct(ProductDto productDto) {
        Product product = dtoConverter.convertToProduct(productDto);

        return reviewRepository.findByProduct(product)
                .stream()
                .map(dtoConverter::convertToReviewDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Map<Integer, Long> getRatingCountsByProduct(ProductDto productDto) {
        Product product = convertToEntity(productDto);
        return reviewRepository.findByProduct(product)
                .stream()
                .collect(Collectors.groupingBy(Review::getRating,Collectors.counting()));
    }

    @Override
    public double getAverageRating(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);

        List<ReviewDto> reviewDtos =
                reviews.stream().map(review -> dtoConverter.convertToReviewDTO(review))
                .toList();

        return reviewDtos.stream()
                .mapToInt(ReviewDto::getRating)
                .average()
                .orElse(0.0);
    }

    public ReviewStatistics calculateStatistics(List<ReviewDto> reviews) {
        int totalReviews = reviews.size();
        double averageRating = reviews.stream().mapToInt(ReviewDto::getRating).average().orElse(0);
        long count5Star = reviews.stream().filter(r -> r.getRating() == 5).count();
        long count4Star = reviews.stream().filter(r -> r.getRating() == 4).count();
        long count3Star = reviews.stream().filter(r -> r.getRating() == 3).count();
        long count2Star = reviews.stream().filter(r -> r.getRating() == 2).count();
        long count1Star = reviews.stream().filter(r -> r.getRating() == 1).count();

        ReviewStatistics stats = new ReviewStatistics();
        stats.setTotalReviews(totalReviews);
        stats.setAverageRating(averageRating);
        stats.setPercent5Star((double) count5Star / totalReviews * 100);
        stats.setPercent4Star((double) count4Star / totalReviews * 100);
        stats.setPercent3Star((double) count3Star / totalReviews * 100);
        stats.setPercent2Star((double) count2Star / totalReviews * 100);
        stats.setPercent1Star((double) count1Star / totalReviews * 100);

        return stats;
    }

    @Data
    public class ReviewStatistics {
        private int totalReviews;
        private double averageRating;
        private double percent5Star;
        private double percent4Star;
        private double percent3Star;
        private double percent2Star;
        private double percent1Star;
    }

    public List<ReviewDto> getAllReviews() {
        List<Review> reviewEntities = reviewRepository.findAll();

        return reviewEntities
                .stream()
                .map(review -> dtoConverter.convertToReviewDTO(review))
                .collect(Collectors.toList());
    }
    public void saveReview(ReviewDto reviewDto) {

        Review review = new Review();
        review.setRating(reviewDto.getRating());
        review.setComment(reviewDto.getComment());
        review.setUser(dtoConverter.convertToEntity(reviewDto.getUserDto()));
        review.setProduct(dtoConverter.convertToProduct(reviewDto.getProductDto()));
        review.setPublishedDate(LocalDateTime.now());


        reviewRepository.save(review);
    }

    private ProductDto convertToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        productDto.setImageUrlShow(product.getImageUrl());
        productDto.setBrand(product.getBrand());
        productDto.setStock(product.getStock());
        productDto.setCategoryId(product.getCategory().getId());

        productDto.setCategory(product.getCategory());

        return productDto;
    }
    private Product convertToEntity(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setImageUrl(productDto.getImageUrlShow());
        product.setBrand(productDto.getBrand());
        product.setStock(productDto.getStock());
        // Set category directly
        return product;
    }
}
