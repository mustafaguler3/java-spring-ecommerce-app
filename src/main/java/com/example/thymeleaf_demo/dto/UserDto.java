package com.example.thymeleaf_demo.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserDto  {

    private int id;
    private String username;
    private String email;
    private String description;
    private String password;
    private MultipartFile profilePicture; // for uploading image file
    private String profilePictureUrl; // for displaying image
    private Boolean isEnabled;

    public UserDto() {
    }
}
