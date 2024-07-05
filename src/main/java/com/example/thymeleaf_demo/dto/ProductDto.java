package com.example.thymeleaf_demo.dto;

import com.example.thymeleaf_demo.domain.Category;
import com.example.thymeleaf_demo.domain.Review;
import com.example.thymeleaf_demo.domain.Wishlist;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class ProductDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private MultipartFile imageUrl;
    private String imageUrlShow;
    private String brand;
    private int stock;
    private Long categoryId;
    private List<ReviewDto> reviews = new ArrayList<ReviewDto>();
    private Category category;
    private double averageRating;

    private boolean inWishlist;

    public ProductDto() {
    }
}
