package com.example.thymeleaf_demo.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int roleId;
    private String roleName;

    @ManyToMany(mappedBy = "roles",cascade = CascadeType.ALL)
    private List<User> users;
}

























