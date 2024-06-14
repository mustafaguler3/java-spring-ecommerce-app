package com.example.thymeleaf_demo.rest;

import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.exception.ResourceNotFoundException;
import com.example.thymeleaf_demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserRepository userRepository;

    @Autowired
    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
