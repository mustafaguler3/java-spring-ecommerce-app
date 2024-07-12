package com.example.thymeleaf_demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class BasketDto {

    private Long id;
    private UserDto userDto;
    private List<BasketItemDto> basketItems;
    private String total;
    private double price;
    private int quantity;

}
