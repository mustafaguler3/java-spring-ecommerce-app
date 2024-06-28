package com.example.thymeleaf_demo.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class UserDto  {

    private long id;
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
