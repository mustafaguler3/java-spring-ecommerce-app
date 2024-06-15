package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.LoginDto;
import com.example.thymeleaf_demo.exception.ResourceNotFoundException;
import com.example.thymeleaf_demo.repository.UserRepository;
import com.example.thymeleaf_demo.service.FileStorageService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final FileStorageService fileStorageService;

    @Autowired
    public HomeController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping(value = {"/", "/home"})
    public String home(Model model) {
        model.addAttribute("currentUser",getCurrentUser().getUsername());
        return "home";
    }

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)){

            String username = authentication.getName();
            User currentUser = userRepository.findByUsername(username);

            if (currentUser == null){
                throw new ResourceNotFoundException("User not found");
            }

            //model.addAttribute("currentUser",currentUser);
            return currentUser;
        }
        return null;
    }

    @GetMapping("/logout-success")
    public String logoutSuccess(Model model){
        return "logout-success";
    }


    @GetMapping("/users")
    public String userList(Model model,
                           @RequestParam(defaultValue = "0",name = "page") int pageNumber,
                           @RequestParam(defaultValue = "4",name = "size") int pageSize,
                           @RequestParam(value = "keyword",required = false) String keyword
                           ){

        if(keyword != null && !keyword.isEmpty()){
            List<User> searchUser = userRepository.searchByUsername(keyword);
            model.addAttribute("searchUser",searchUser);

        }
            Page<User> users = userRepository.findAll(PageRequest.of(pageNumber, pageSize));
            model.addAttribute("userPage", users);

            if(users.isEmpty()){
                throw new ResourceNotFoundException("No users found");
            }
            model.addAttribute("keyword",keyword);

        return "user-list";
    }

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource file = fileStorageService.loadFileAsResource(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id){
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()){
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable("id") Integer id, Model model){
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("user",user.get());
        return "edit-form";
    }
    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Integer id,@ModelAttribute("user") User user){
        Optional<User> findedUser = userRepository.findById(id);

        if (!findedUser.isPresent()){
            throw new ResourceNotFoundException("User not found");
        }

        findedUser.get().setUsername(user.getUsername());
        findedUser.get().setEmail(user.getEmail());
        findedUser.get().setPassword(user.getPassword());
        userRepository.save(findedUser.get());

        return "redirect:/users";
    }


}





















