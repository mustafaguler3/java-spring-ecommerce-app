package com.example.thymeleaf_demo.service;

import com.example.thymeleaf_demo.domain.Order;

public interface OrderService {
    Order findOrderById(Long orderId);
    Order saveOrder(Order order);
}
