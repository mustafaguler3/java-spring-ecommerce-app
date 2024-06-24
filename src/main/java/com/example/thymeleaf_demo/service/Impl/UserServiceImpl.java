package com.example.thymeleaf_demo.service.Impl;

import com.example.thymeleaf_demo.domain.PasswordResetToken;
import com.example.thymeleaf_demo.domain.Role;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.domain.VerificationToken;
import com.example.thymeleaf_demo.dto.PasswordResetTokenDto;
import com.example.thymeleaf_demo.dto.RegisterDto;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.exception.FileStorageException;
import com.example.thymeleaf_demo.exception.ResourceNotFoundException;
import com.example.thymeleaf_demo.repository.PasswordResetTokenRepository;
import com.example.thymeleaf_demo.repository.RoleRepository;
import com.example.thymeleaf_demo.repository.UserRepository;
import com.example.thymeleaf_demo.repository.VerificationTokenRepository;
import com.example.thymeleaf_demo.service.EmailService;
import com.example.thymeleaf_demo.service.FileStorageService;
import com.example.thymeleaf_demo.service.UserService;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

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

    @Override
    public void saveUser(UserDto userDto)  {
        UserDto existUser = findByEmail(userDto.getEmail());

        // Validate inputs
        if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

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

                    String fileName = fileStorageService.storeFile(profilePicture);
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
    public User findById(Integer id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()){
            return user.get();
        }
        return null;
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
    public void deleteUser(User user) {
        userRepository.deleteById(user.getId());
    }

    @Override
    public void updateUser(User user) {
        User user1 = userRepository.findById(user.getId()).orElse(null);
        assert user1 != null;
        user1.setUsername(user.getUsername());
        user1.setPassword(passwordEncoder.encode(user.getPassword()));
        user1.setEmail(user.getEmail());
        userRepository.save(user1);


    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null){
            return null;
        }

        return convertToDto(user);
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email);

        if (user != null){
            return convertToDto(user);
        }

        return null;
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setDescription(user.getDescription());
        userDto.setIsEnabled(user.getIsEnabled()); // Convert Boolean field
        userDto.setProfilePictureUrl(user.getProfilePicture()); // Set the profile picture URL
        return userDto;
    }

    private User convertToEntity(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setDescription(userDto.getDescription());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setIsEnabled(userDto.getIsEnabled());
        user.setProfilePicture(userDto.getProfilePictureUrl()); // Set the profile picture URL
        return user;
    }
}
