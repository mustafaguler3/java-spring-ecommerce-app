package com.example.thymeleaf_demo.rest;

import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.exception.ResourceNotFoundException;
import com.example.thymeleaf_demo.repository.UserRepository;
import com.example.thymeleaf_demo.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public UserRestController(UserRepository userRepository, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }
    @GetMapping("/images/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<java.nio.file.Path> getFile(
            @PathVariable("fileName") String fileName) {
        Path resource = fileStorageService.loadFile(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG) // or MediaType.IMAGE_PNG
                .body(resource);
    }

    @GetMapping
    public ResponseEntity<?> userList(Model model,
                                   @RequestParam(defaultValue = "0") int pageNumber,
                                   @RequestParam(defaultValue = "4") int pageSize){

        List<User> users = userRepository.findAll();

        if(users.isEmpty()){
            throw new ResourceNotFoundException("No users found");
        }

        Page<User> pagedUsers = new PageImpl<>(users,PageRequest.of(pageNumber,pageSize),users.size());

        return new ResponseEntity<>(pagedUsers,HttpStatus.OK);
    }
}
