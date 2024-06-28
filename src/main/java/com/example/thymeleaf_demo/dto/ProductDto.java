package com.example.thymeleaf_demo.dto;

import com.example.thymeleaf_demo.domain.Category;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class ProductDto {

    private int id;
    private String name;
    private BigDecimal price;
    private String description;
    private MultipartFile imageUrl;
    private String imageUrlShow;
    private int stock;
    private int quantity;
    private int categoryId;
    private Category category;
}
