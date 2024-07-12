package com.example.thymeleaf_demo.util;

import com.example.thymeleaf_demo.domain.*;
import com.example.thymeleaf_demo.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DTOConverter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DTOConverter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Category convertToCategory(CategoryDto categoryDto){
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());

        Set<Product> products =
                categoryDto.getProductDtos().stream()
               .map(p -> {
                    Product product = new Product();
                    product.setId(p.getId());
                    product.setName(p.getName());
                    product.setPrice(p.getPrice());
                    product.setDescription(p.getDescription());
                    product.setBrand(p.getBrand());
                    product.setStock(p.getStock());
                    product.setCategory(category);
                    return product;

               })
               .collect(Collectors.toSet());

        category.setProducts(products);

        return category;
    }

    public CategoryDto convertToCategoryDto(Category category){

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());

        Set<ProductDto> productDtos = category.getProducts().stream()
                .map(product -> convertToProductDTO(product))
                .collect(Collectors.toSet());

        categoryDto.setProductDtos(productDtos);



        return categoryDto;
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

        Set<WishlistDto> wishlistDtos =
                product.getWishlists()
                                .stream()
                                        .map(this::convertToDto)
                                                .collect(Collectors.toSet());

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
        userDto.setId((long) user.getId());
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

    public Wishlist convertToEntity(WishlistDto wishlistDto){
        Wishlist wishlist = new Wishlist();
        wishlist.setId(wishlistDto.getId());
        return wishlist;
    }

    public WishlistDto convertToDto(Wishlist wishlist) {
        WishlistDto wishlistDto = new WishlistDto();
        wishlistDto.setId(wishlist.getId());
        wishlistDto.setUserId((long) wishlist.getUser().getId());
        wishlistDto.setProductId(wishlist.getProduct().getId());
        return wishlistDto;
    }

    public static OrderItemDto toDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        OrderItemDto dto = new OrderItemDto();
        dto.setId(orderItem.getId());
        dto.setOrderId(orderItem.getOrder() != null ? orderItem.getOrder().getId() : null);
        dto.setProductId(orderItem.getProduct() != null ? orderItem.getProduct().getId() : null);
        dto.setProductName(orderItem.getProduct() != null ? orderItem.getProduct().getName() : null);
        dto.setPrice(orderItem.getPrice());
        dto.setQuantity(orderItem.getQuantity());

        return dto;
    }

    public static OrderItem toEntity(OrderItemDto dto, Order order, Product product) {
        if (dto == null) {
            return null;
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setId(dto.getId());
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setPrice(dto.getPrice());
        orderItem.setQuantity(dto.getQuantity());

        return orderItem;
    }

    public List<OrderItemDto> toDtoList(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(DTOConverter::toDto)
                .collect(Collectors.toList());
    }

    public static List<OrderItem> toEntityList(List<OrderItemDto> dtos, Order order, List<Product> products) {
        return dtos.stream()
                .map(dto -> toEntity(dto, order, products.stream().filter(p -> p.getId().equals(dto.getProductId())).findFirst().orElse(null)))
                .collect(Collectors.toList());
    }



}



















