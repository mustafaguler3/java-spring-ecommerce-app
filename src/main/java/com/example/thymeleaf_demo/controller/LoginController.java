package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.Basket;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.service.CartService;
import com.example.thymeleaf_demo.service.UserService;
import com.example.thymeleaf_demo.util.DTOConverter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final CartService cartService;
    private final DTOConverter dtoConverter;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager, UserService userService, CartService cartService, DTOConverter dtoConverter) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.cartService = cartService;
        this.dtoConverter = dtoConverter;
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
                        Model model,
                        HttpSession session) {
        UserDto user = userService.findByEmail(userDto.getEmail());

        if (user == null) {
            model.addAttribute("errorMsg", "There is no account associated with this email address.");
            return "login";
        }

        if (!user.getIsEnabled()) {
            model.addAttribute("errorMsg", "Please verify your email address before logging in.");
            return "login";
        }
        Basket basket = cartService.findOrCreateCartForUser(dtoConverter.convertToEntity(userDto));
        session.setAttribute("cart", basket);
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

