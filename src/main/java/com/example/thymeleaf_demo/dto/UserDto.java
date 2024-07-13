package com.example.thymeleaf_demo.dto;

import com.example.thymeleaf_demo.domain.Wishlist;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserDto  {

    private Long id;
    private String username;
    private String email;
    private String description;
    private String password;
    private MultipartFile profilePicture; // for uploading image file
    private String profilePictureUrl; // for displaying image
    private Boolean isEnabled;

    private String address;
    private String address2;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String phone;

    private String firstName;
    private String lastName;


    private List<WishlistDto> wishlists = new ArrayList<WishlistDto>();

    public UserDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public UserDto() {
    }
}
