package com.example.thymeleaf_demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RegisterDto {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    @Email
    private String email;
    private MultipartFile profilePicture;
}
