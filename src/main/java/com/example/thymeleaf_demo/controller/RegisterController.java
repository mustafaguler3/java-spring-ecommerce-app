package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.Role;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.RegisterDto;
import com.example.thymeleaf_demo.repository.RoleRepository;
import com.example.thymeleaf_demo.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Optional;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user")
                                   @Valid User user1, BindingResult bindingResult, Model model) {
        User userDb = userRepository.findByEmail(user1.getEmail());

        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (userDb == null) {

            User user = new User();
            user.setUsername(user1.getUsername());
            user.setPassword(passwordEncoder.encode(user1.getPassword()));
            user.setEmail(user1.getEmail());

            Role userRole = roleRepository.findByRoleName("ROLE_USER");
            user.setRoles(Collections.singleton(userRole));

            userRepository.save(user);
        }else {
            model.addAttribute("registrationError", "Username already exists.");
            return "register";
        }


        return "redirect:/login";
    }
}
