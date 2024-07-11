package com.example.thymeleaf_demo.dto;

import lombok.Data;

import java.util.List;
@Data
public class BasketDto {

    private List<BasketItemDto> cartItems;
    private String total;
    private double price;
    private int quantity;



}
