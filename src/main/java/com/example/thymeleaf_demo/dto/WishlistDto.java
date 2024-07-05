package com.example.thymeleaf_demo.dto;

import com.example.thymeleaf_demo.domain.Product;
import com.example.thymeleaf_demo.domain.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class WishlistDto {

    private Long id; // Assuming this is the wishlist item ID
    private Long productId;
    private Long userId; //

    private String imageUrl;
    private BigDecimal price;
    private int stock;
    private String productName;

    public WishlistDto() {
    }


}
