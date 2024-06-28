package com.example.thymeleaf_demo.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@Entity
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Username must not be blank")
    private String username;
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be right format")
    private String email;
    @NotBlank(message = "Password must not be blank")
    @Size(min = 3, message = "Password must be at least 6 characters")
    private String password;
    @NotBlank(message = "Description must not be blank")
    @Size(min = 10,max = 1000)
    private String description;
    private String profilePicture;

    /*@OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Cart cart;*/

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private VerificationToken verificationToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private PasswordResetToken passwordResetToken;

    private Boolean isEnabled = false;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {
    }


}

























