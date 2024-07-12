package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.Order;
import com.example.thymeleaf_demo.domain.OrderItem;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.service.OrderService;
import com.example.thymeleaf_demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String getUserOrders(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        UserDto currentUser = userService.findByUsername(currentUsername);
        List<Order> orders = orderService.findOrderByUserId(currentUser.getId());

        model.addAttribute("orders", orders);

        for (Order order : orders){
            model.addAttribute("orderItems",order.getOrderItems());
        }


        return "orders";
    }

}



















