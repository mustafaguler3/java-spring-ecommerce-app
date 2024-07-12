package com.example.thymeleaf_demo.service.Impl;

import com.example.thymeleaf_demo.domain.Order;
import com.example.thymeleaf_demo.domain.OrderItem;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.exception.ResourceNotFoundException;
import com.example.thymeleaf_demo.repository.OrderItemRepository;
import com.example.thymeleaf_demo.repository.OrderRepository;
import com.example.thymeleaf_demo.repository.UserRepository;
import com.example.thymeleaf_demo.service.BasketService;
import com.example.thymeleaf_demo.service.OrderService;
import com.example.thymeleaf_demo.service.UserService;
import com.example.thymeleaf_demo.util.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BasketService basketService;
    @Autowired
    private DTOConverter dtoConverter;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public Order saveOrder(Order order) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        UserDto userDto = userService.findByUsername(currentUsername);

        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Pending");
        order.setUser(dtoConverter.convertToEntity(userDto));

        Order savedOrder = orderRepository.save(order);

        for (OrderItem item : order.getOrderItems()){
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        }

        return savedOrder;
    }

    @Override
    public List<Order> findOrderByUserId(Long userId) {
        List<Order> order = orderRepository.findOrderByUserId(userId);

        return order;
    }
}
















