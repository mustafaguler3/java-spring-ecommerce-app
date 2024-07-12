package com.example.thymeleaf_demo.service.Impl;

import com.example.thymeleaf_demo.domain.*;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.exception.FileStorageException;
import com.example.thymeleaf_demo.exception.ResourceNotFoundException;
import com.example.thymeleaf_demo.repository.*;
import com.example.thymeleaf_demo.service.EmailService;
import com.example.thymeleaf_demo.service.FileStorageService;
import com.example.thymeleaf_demo.service.UserService;
import com.example.thymeleaf_demo.util.DTOConverter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EmailService emailService;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private DTOConverter dtoConverter;

    @Autowired
    private BasketRepository basketRepository;


    @Override
    public void saveUser(UserDto userDto)  {
        UserDto existUser = findByEmail(userDto.getEmail());


        if (existUser != null){
            throw new RuntimeException("Email already exists");
        }

            User user1 = new User();
            user1.setUsername(userDto.getUsername());
            user1.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user1.setEmail(userDto.getEmail());
            user1.setDescription(userDto.getDescription());
            user1.setIsEnabled(false);

            Role role = roleRepository.findByRoleName("ROLE_USER");
            if(role != null){
                user1.setRoles(Collections.singleton(role));
            }else {
                Role newRole = new Role();
                newRole.setRoleName("ROLE_USER");
                roleRepository.save(newRole);
                user1.setRoles(Collections.singleton(newRole));
            }

            //MultipartFile mapToMultipart = modelMapper.map(user.getProfilePicture(),MultipartFile.class);
            MultipartFile profilePicture = userDto.getProfilePicture();

            if (profilePicture != null && !profilePicture.isEmpty()){
                try {

                    String fileName = fileStorageService.storeFile(profilePicture,"users");
                    user1.setProfilePicture(fileName);

                } catch (FileStorageException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Failed to upload profile picture");
                }
            }
        userRepository.save(user1);

        //UserDto mapToDto = convertToDto(user1);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user1);
        verificationToken.setExpiryDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)); // 24 saat geçerlilik
        verificationTokenRepository.save(verificationToken);

        Basket basket = new Basket();
        basket.setUser(user1);
        basketRepository.save(basket);


        emailService.sendVerificationEmail(user1, token);

    }

    @Override
    public boolean verifyUser(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken != null) {
            User user = verificationToken.getUser();
            user.setIsEnabled(true);
            userRepository.save(user);
            verificationTokenRepository.delete(verificationToken);
            return true;
        }
        return false;
    }

    @Override
    public UserDto findById(int id) {
        Optional<User> user = userRepository.findById((long) id);

        return user.map(this::convertToDto).orElse(null);

    }

    @Override
    public List<User> searchByUsername(String keyword) {
        return userRepository.searchByUsername(keyword);
    }

    @Override
    public Page<User> findAll(PageRequest pageRequest) {
        Page<User> users = userRepository.findAll(pageRequest);

        if (users == null){
            throw new ResourceNotFoundException("User not found");
        }

        return users;
    }

    @Override
    public String createPasswordResetToken(User user) {

        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // 30 dakika geçerlilik

        passwordResetTokenRepository.save(passwordResetToken);

        return token;
    }

    @Override
    public boolean validatePasswordResetToken(String token){
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if (passwordResetToken == null){
            return false;
        }
        //PasswordResetTokenDto tokenDto = modelMapper.map(passwordResetToken,PasswordResetTokenDto.class);
        return !passwordResetToken.isExpired();
    }

    @Override
    public User getUserByPasswordResetToken(String token){
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);

        if (resetToken == null){
            return null;
        }

        return resetToken.getUser();
    }

    @Override
    public void updatePassword(User user,String newPassword){
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
        passwordResetTokenRepository.deleteByUser(user);
    }

    @Override
    public void deleteUser(UserDto userDto) {
        userRepository.deleteById((long) userDto.getId());
    }

    @Override
    public void updateUser(UserDto userDto) {
        User user = userRepository.findById((long) userDto.getId())
                .orElse(null);

        if (user != null){
            user.setUsername(userDto.getUsername());
            //user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            //user.setEmail(userDto.getEmail());
            user.setDescription(userDto.getDescription());
            if (userDto.getProfilePictureUrl() != null){
                user.setProfilePicture(userDto.getProfilePictureUrl());
            }
        }
        userRepository.save(user);
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null){
            return null;
        }
;
        return dtoConverter.convertToDto(user);
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email);

        if (user != null){
            return dtoConverter.convertToDto(user);
        }

        return null;
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId((long) user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword()); // Set the password as plain text for security reasons
        userDto.setDescription(user.getDescription());
        userDto.setIsEnabled(user.getIsEnabled()); // Convert Boolean field
        userDto.setProfilePictureUrl(user.getProfilePicture()); // Set the profile picture URL
        return userDto;
    }

    private User convertToEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setDescription(userDto.getDescription());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setIsEnabled(userDto.getIsEnabled());
        user.setProfilePicture(userDto.getProfilePictureUrl()); // Set the profile picture URL

        return user;
    }
}
