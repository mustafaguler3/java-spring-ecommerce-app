package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.Role;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.RegisterDto;
import com.example.thymeleaf_demo.exception.FileStorageException;
import com.example.thymeleaf_demo.repository.RoleRepository;
import com.example.thymeleaf_demo.repository.UserRepository;
import com.example.thymeleaf_demo.service.FileStorageService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model
                                       ) {
        model.addAttribute("user", new RegisterDto());

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid RegisterDto user1,
                               BindingResult bindingResult,
                               Model model){
        User userDb = userRepository.findByEmail(user1.getEmail());
        if (userDb != null) {
            bindingResult.rejectValue("email", null, "There is already an account registered with this email.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user1); // Kullanıcının verilerini geri yüklüyoruz
            return "register";
        }

        User user = new User();
        user.setUsername(user1.getUsername());
        user.setPassword(passwordEncoder.encode(user1.getPassword()));
        user.setEmail(user1.getEmail());

        MultipartFile profilePicture = user1.getProfilePicture();

        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                String fileName = fileStorageService.storeFile(profilePicture);
                user.setProfilePicture(fileName);
            } catch (FileStorageException e) {
                e.printStackTrace();
                bindingResult.rejectValue("profilePicture", null, "Failed to upload profile picture");
                model.addAttribute("user", user1); // Kullanıcının verilerini geri yüklüyoruz
                return "register";
            }
        }

        Role userRole = roleRepository.findByRoleName("ROLE_USER");
        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
        return "redirect:/register?success";
    }




}




















