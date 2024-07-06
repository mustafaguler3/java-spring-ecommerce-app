package com.example.thymeleaf_demo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class CartDto {
    private List<CartItemDto> cartItems;
    private String total;
}
