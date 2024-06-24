package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.PasswordResetToken;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.PasswordResetTokenDto;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.service.EmailService;
import com.example.thymeleaf_demo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotPasswordController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Autowired
    public ForgotPasswordController(UserService userService, ModelMapper modelMapper, EmailService emailService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordShow(Model model){
        model.addAttribute("userDto",new UserDto());
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam(value = "email",required = false) String email,
                                 Model model) throws Exception {

        UserDto user = userService.findByEmail(email);
        if (user == null){
            model.addAttribute("error","No user found with that email address");
            return "forgot-password";
        }

        User userMap = modelMapper.map(user,User.class);

        String token = userService.createPasswordResetToken(userMap);
        String resetLink = "http://localhost:8080/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(userMap,resetLink);

        model.addAttribute("message","Password reset link has been sent to your email");
        return "forgot-password";
    }

}

