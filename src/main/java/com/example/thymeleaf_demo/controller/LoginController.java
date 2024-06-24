package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.LoginDto;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.repository.UserRepository;
import com.example.thymeleaf_demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model,
                                @RequestParam(value = "error",required = false) String error) {
        model.addAttribute("loginDto",new UserDto());

        if (error != null) {
            model.addAttribute("errorMsg","Invalid username or password");
            return "login";
        }

        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginDto") @Valid UserDto userDto,
                        BindingResult bindingResult,
                        Model model) {
        UserDto user = userService.findByEmail(userDto.getEmail());

        if (user == null) {
            model.addAttribute("errorMsg", "There is no account associated with this email address.");
            return "login";
        }

        if (!user.getIsEnabled()) {
            model.addAttribute("errorMsg", "Please verify your email address before logging in.");
            return "login";
        }

        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response,
                         Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null){
            new SecurityContextLogoutHandler().logout(request, response,authentication);
        }

        model.addAttribute("message", "You have been logged out successfully.");
        return "redirect:/login?logout";
    }

}

