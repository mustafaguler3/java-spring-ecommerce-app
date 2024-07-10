package com.example.thymeleaf_demo.domain;

import lombok.Data;

@Data
public class OrderSummary {
    private Double subtotal;
    private Double shippingCost;
    private Double discount;
    private Double total;
}
