package com.example.thymeleaf_demo.service.Impl;

import com.example.thymeleaf_demo.domain.Order;
import com.example.thymeleaf_demo.domain.TrackingInfo;
import com.example.thymeleaf_demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackingService {
    @Autowired
    private OrderRepository orderRepository;

    public TrackingInfo getTrackingInfo(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow();

        return new TrackingInfo(order);
    }
}




















