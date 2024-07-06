package com.example.thymeleaf_demo.dto;

import lombok.Data;

@Data
public class CartItemDto {
    private String imageUrl;
    private String description;
    private String brand;
private String productName;
private int quantity;
private String price;
private String subtotal;
}
