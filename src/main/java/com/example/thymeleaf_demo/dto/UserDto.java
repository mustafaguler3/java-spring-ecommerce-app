package com.example.thymeleaf_demo.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserDto  {

    private int id;
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 3, message = "Password must be at least 6 characters")
    private String password;
    @NotBlank(message = "Description must not be blank")
    @Size(min = 10,max = 1000)
    private String description;

    private MultipartFile profilePicture; // for uploading image file
    private String profilePictureUrl; // for displaying image

    private Boolean isEnabled;

}
