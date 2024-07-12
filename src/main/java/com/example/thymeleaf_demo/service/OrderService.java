package com.example.thymeleaf_demo.service;

import com.example.thymeleaf_demo.domain.Order;

import java.util.List;

public interface OrderService {
    Order findOrderById(Long orderId);
    Order saveOrder(Order order);
    List<Order> findOrderByUserId(Long userId);
}
