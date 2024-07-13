package com.example.thymeleaf_demo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TrackingInfo {

    private Long orderId;
    private String trackingNumber;
    private String status;
    private String expectedArrival;
    private List<Order> orders;

    public TrackingInfo(Order order) {
        this.orders = order.getUser().getOrders();
        this.orderId = order.getId();
        this.expectedArrival = order.getExpectedArrival().toString();
        this.status = order.getStatus(); // Example, set a fixed status
        this.trackingNumber = order.getTrackingNumber(); // Example, generate a tracking number
    }
}



















