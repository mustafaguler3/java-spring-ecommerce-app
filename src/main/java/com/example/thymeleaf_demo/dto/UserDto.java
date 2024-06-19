package com.example.thymeleaf_demo.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserDto {

    private int id;
    private String username;
    private String email;
    private MultipartFile profilePicture;
    private String password;
    private Boolean isEnabled;
}
