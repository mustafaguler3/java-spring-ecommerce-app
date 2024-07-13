package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.Order;
import com.example.thymeleaf_demo.domain.OrderItem;
import com.example.thymeleaf_demo.domain.TrackingInfo;
import com.example.thymeleaf_demo.service.Impl.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class TrackingController {
    @Autowired
    private TrackingService trackingService;

    @GetMapping("/track/{orderId}")
    public String trackOrder(@PathVariable Long orderId, Model model){
        TrackingInfo trackingInfo = trackingService.getTrackingInfo(orderId);
        model.addAttribute("trackingInfo", trackingInfo);

        for (Order order : trackingInfo.getOrders()){
            for (OrderItem orderItems : order.getOrderItems()){
                model.addAttribute("orderItems", orderItems);
            }
        }
        return "tracking";
    }
}



















