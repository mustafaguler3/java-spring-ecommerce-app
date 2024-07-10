package com.example.thymeleaf_demo.dto;

import com.example.thymeleaf_demo.domain.Coupon;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class CartDto {

    private List<CartItemDto> cartItems;
    private String total;
    private double price;
    private int quantity;



}
