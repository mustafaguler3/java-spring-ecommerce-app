package com.example.thymeleaf_demo.dto;

import com.example.thymeleaf_demo.domain.Product;
import com.example.thymeleaf_demo.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@ToString
public class ReviewDto {
    private int reviewId;
    private String username;
    private UserDto userDto;
    private int rating;
    private String comment;
    private LocalDateTime publishedDate;
    private ProductDto productDto;
    private String profilePicture;


    public ReviewDto() {
    }
}
