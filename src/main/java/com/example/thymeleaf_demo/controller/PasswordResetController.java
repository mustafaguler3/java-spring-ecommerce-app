package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.PasswordResetToken;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.repository.PasswordResetTokenRepository;
import com.example.thymeleaf_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordResetController {

    private final UserService userService;

    @Autowired
    public PasswordResetController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {

        if (token == null || token.isEmpty()) {
            model.addAttribute("message", "Invalid or missing token");
            return "error";
        }

        boolean isValidToken = userService.validatePasswordResetToken(token);

        if (isValidToken) {
            model.addAttribute("token", token);
            return "reset-password";
        } else {
            model.addAttribute("message", "Invalid or expired token");
            return "error";
        }
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("password") String newPassword,
                                       @RequestParam("confirmPassword") String confirmPassword,
                                       Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("message", "Passwords do not match");
            return "reset-password";
        }

        UserDto user = userService.getUserByPasswordResetToken(token);

        if (user == null) {
            model.addAttribute("message", "Invalid or expired token");
            return "error";
        }

        userService.updatePassword(user, newPassword);
        model.addAttribute("message", "Your password has been successfully reset");
        return "login";
    }
}
