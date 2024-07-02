package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.exception.FileStorageException;
import com.example.thymeleaf_demo.exception.ResourceNotFoundException;
import com.example.thymeleaf_demo.repository.PasswordResetTokenRepository;
import com.example.thymeleaf_demo.service.EmailService;
import com.example.thymeleaf_demo.service.FileStorageService;

import com.example.thymeleaf_demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Controller
public class UsersController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final FileStorageService fileStorageService;
    private final EmailService emailService;
    private final ModelMapper modelMapper;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    public UsersController(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, FileStorageService fileStorageService, EmailService emailService, ModelMapper modelMapper, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.fileStorageService = fileStorageService;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @GetMapping("/logout-success")
    public String logoutSuccess(Model model){
        return "/login?logout";
    }

    @GetMapping("/users")
    public String userList(Model model,
                           @RequestParam(defaultValue = "0",name = "page") int pageNumber,
                           @RequestParam(defaultValue = "4",name = "size") int pageSize,
                           @RequestParam(value = "keyword",required = false) String keyword
                           ){

        if(keyword != null && !keyword.isEmpty()){
            List<User> searchUser = userService.searchByUsername(keyword);
            model.addAttribute("searchUser",searchUser);

        }
            Page<User> users = userService.findAll(PageRequest.of(pageNumber, pageSize));
            model.addAttribute("userPage", users);

            if(users.isEmpty()){
                throw new ResourceNotFoundException("No users found");
            }
            model.addAttribute("keyword",keyword);

        return "user-list";
    }

    @GetMapping("/uploads/{fileType}/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename,
                                             @PathVariable String fileType) {

        Resource file = fileStorageService.loadFile(filename,fileType);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id){
        UserDto userDto = userService.findById(id);

        if (userDto == null){
            throw new ResourceNotFoundException("User not found");
        }

        userService.deleteUser(userDto);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model){
        UserDto user = userService.findById(id);
        model.addAttribute("user",user);
        return "edit-form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id,
                           @ModelAttribute("user") UserDto userDto,
                           BindingResult bindingResult,
                           Model model,
                           @RequestParam("profilePicture") MultipartFile profilePicture) throws Exception {

        UserDto findedUser = userService.findById(id);

        if (findedUser == null){
            throw new ResourceNotFoundException("User not found");
        }

        if (!profilePicture.isEmpty()){
            try {
                String fileName = fileStorageService.storeFile(profilePicture,"products");
                Resource filePath = fileStorageService.loadFile(fileName,"users");

                if(!filePath.toString().equals(findedUser.getProfilePicture())){
                    fileStorageService.deleteFile(String.valueOf(findedUser.getProfilePicture()));
                }
                MultipartFile multipartFile = userDto.getProfilePicture();

                findedUser.setProfilePicture(multipartFile);
                model.addAttribute("success","Photo updated successfuly");

                return "user-list";
            }catch (FileStorageException e){
                e.printStackTrace();
                bindingResult.rejectValue("profilePicture",e.getMessage(),"Failed to upload profile picture");
                return "user-list";
            }
        }

        findedUser.setUsername(userDto.getUsername());
        findedUser.setEmail(userDto.getEmail());
        //findedUser.setPassword(userDto.getPassword());

        userService.saveUser(userDto);

        return "redirect:/edit/"+userDto.getId();
    }





}





















