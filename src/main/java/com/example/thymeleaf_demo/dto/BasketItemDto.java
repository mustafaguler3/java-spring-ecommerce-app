package com.example.thymeleaf_demo.dto;

import com.example.thymeleaf_demo.domain.Coupon;
import lombok.Data;

@Data
public class BasketItemDto {
    private Long productId;
    private String imageUrl;
    private String description;
    private String brand;
    private String productName;
    private int quantity;
    private double price;
    private String subtotal;

    public Double getTotalAmount(){
        return price * quantity;
    }

    public Double getDiscountAmount(Coupon coupon){
        if (coupon == null || !coupon.isValid()){
            return getTotalAmount();
        }

        double discount = price * (coupon.getDiscountAmount() / 100);

        return (price - discount) * quantity;
    }

}
