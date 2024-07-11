package com.example.thymeleaf_demo.dto;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private Double price;
    private int quantity;

    public OrderItemDto() {
    }

    public OrderItemDto(Long id, Long orderId, Long productId, String productName, Double price, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }
}
