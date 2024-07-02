package com.example.thymeleaf_demo.dto;

import com.example.thymeleaf_demo.domain.Category;
import com.example.thymeleaf_demo.domain.Review;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
}
