package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.service.FileStorageService;
import com.example.thymeleaf_demo.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProfileController {

    private final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileController(UserService userService, FileStorageService fileStorageService, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.fileStorageService = fileStorageService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public String getUserProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            UserDto currentUser = userService.findByUsername(currentUsername);
            if (currentUser != null) {

                model.addAttribute("currentUser", currentUser);
                return "profile";
            } else {
                model.addAttribute("error", "User not found");
            }
        } else {
            model.addAttribute("error", "You must be logged in to view this page");
        }

        return "profile";
    }

    @GetMapping("/profile/edit/{id}")
    public String editUserProfile(@PathVariable int id,Model model){
        UserDto currentUser = userService.findById(id);
        if (currentUser != null) {
            logger.info("Edit user profile id {}", currentUser.getId());
            model.addAttribute("currentUser", currentUser);
        } else {
            model.addAttribute("error", "User not found");
        }
        return "edit-profile";
    }

    @PostMapping("/profile/edit")
    public String editUserProfile(@ModelAttribute("currentUser") @Valid UserDto userDto,
                                  Model model,
                                  BindingResult bindingResult){
       if (bindingResult.hasErrors()){
           return "edit-profile";
       }
       try {
           MultipartFile file = userDto.getProfilePicture();
           if (!file.isEmpty()){
               String fileName = fileStorageService.storeFile(file,"users");
               userDto.setProfilePictureUrl(fileName);
           }
           userService.updateUser(userDto);

           // retrieve updated user information
           UserDto updatedUser = userService.findById(Math.toIntExact(userDto.getId()));
           model.addAttribute("currentUser",updatedUser);

           model.addAttribute("success","Profile updated successfully");
           return "redirect:/profile";

       }catch (Exception e){
           model.addAttribute("error",e.getMessage());
       }
        return "redirect:/profile";
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(passwordEncoder.encode(user.getPassword()));
        userDto.setIsEnabled(user.getIsEnabled()); // Convert Boolean field
        userDto.setProfilePictureUrl(user.getProfilePicture()); // Set the profile picture URL
        return userDto;
    }

    private User convertToEntity(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setIsEnabled(userDto.getIsEnabled());
        user.setProfilePicture(userDto.getProfilePictureUrl()); // Set the profile picture URL
        return user;
    }



}























