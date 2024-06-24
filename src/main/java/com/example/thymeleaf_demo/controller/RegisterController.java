package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.domain.VerificationToken;
import com.example.thymeleaf_demo.dto.RegisterDto;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.repository.RoleRepository;
import com.example.thymeleaf_demo.repository.VerificationTokenRepository;
import com.example.thymeleaf_demo.service.EmailService;
import com.example.thymeleaf_demo.service.FileStorageService;
import com.example.thymeleaf_demo.service.UserService;
import com.example.thymeleaf_demo.util.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.util.*;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private EmailService emailService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model
                                       ) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDto userDto,
                               Model model,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return "register";
        }
            userService.saveUser(userDto);
            return "redirect:/register?success";
    }

    @GetMapping("/verify")
    public String confirmRegistration(@RequestParam("token") String token,
                                      Model model) {

        boolean isVerified = userService.verifyUser(token);

        if (isVerified) {
            model.addAttribute("message", "Email başarıyla doğrulandı. Artık giriş yapabilirsiniz.");
        } else {
            model.addAttribute("message", "Doğrulama başarısız. Token geçersiz veya süresi dolmuş olabilir.");
        }
        return "verificationResult";
    }

}




















