package com.example.thymeleaf_demo.service.Impl;

import com.example.thymeleaf_demo.domain.PasswordResetToken;
import com.example.thymeleaf_demo.domain.Role;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.domain.VerificationToken;
import com.example.thymeleaf_demo.dto.PasswordResetTokenDto;
import com.example.thymeleaf_demo.dto.RegisterDto;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.exception.FileStorageException;
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
    public void saveUser(UserDto userDto) throws Exception {
        UserDto existUser = findByEmail(userDto.getEmail());

        if (existUser != null){
            throw new RuntimeException("Email already exists");
        }

            User user1 = new User();

            user1.setUsername(userDto.getUsername());
            user1.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user1.setEmail(userDto.getEmail());
            user1.setIsEnabled(false);

            Role role = roleRepository.findByRoleName("ROLE_USER");
            if(role != null){
                role.setRoleName(role.getRoleName());
                roleRepository.save(role);
            }
            user1.setRoles(Collections.singleton(role));

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
        User savedUser = userRepository.save(user1);
        UserDto savedUserDto = modelMapper.map(savedUser,UserDto.class);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user1);
        verificationToken.setExpiryDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)); // 24 saat geçerlilik
        verificationTokenRepository.save(verificationToken);

        emailService.sendVerificationEmail(savedUserDto, token);

    }

    @Override
    public boolean verifyUser(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken != null) {
            User user = verificationToken.getUser();
            user.setIsEnabled(true);

            User savedUser = userRepository.save(user);
            modelMapper.map(savedUser,UserDto.class);

            verificationTokenRepository.delete(verificationToken);
            return true;
        }
        return false;
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> searchByUsername(String keyword) {
        return userRepository.searchByUsername(keyword);
    }

    @Override
    public Page<User> findAll(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest);
    }

    @Override
    public String createPasswordResetToken(UserDto user) {

        String token = UUID.randomUUID().toString();
        PasswordResetTokenDto passwordResetTokenDto = new PasswordResetTokenDto();
        passwordResetTokenDto.setToken(token);
        passwordResetTokenDto.setUserDto(user);
        passwordResetTokenDto.setExpiryDate(LocalDateTime.now().plusHours(24)); // 30 dakika geçerlilik

        PasswordResetToken passwordResetToken = modelMapper.map(passwordResetTokenDto,PasswordResetToken.class);
        passwordResetTokenRepository.save(passwordResetToken);

        return token;
    }

    @Override
    public boolean validatePasswordResetToken(String token){
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if (passwordResetToken == null){
            return false;
        }
        PasswordResetTokenDto tokenDto = modelMapper.map(passwordResetToken,PasswordResetTokenDto.class);
        return !tokenDto.isExpired();
    }

    @Override
    public UserDto getUserByPasswordResetToken(String token){
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);

        if (resetToken == null){
            return null;
        }

        PasswordResetTokenDto tokenDto = modelMapper.map(resetToken,PasswordResetTokenDto.class);

        return tokenDto.getUserDto();
    }

    @Override
    public void updatePassword(UserDto userDto,String newPassword){
        User user = modelMapper.map(userDto,User.class);

        userDto.setPassword(passwordEncoder.encode(newPassword));

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
        user1.setUsername(user.getUsername());
        user1.setPassword(user.getPassword());
        user1.setEmail(user.getEmail());
        userRepository.save(user1);
    }

    @Override
    public UserDto findByUsername(String username) throws Exception {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new Exception("User not found");
        }

        UserDto userDto = modelMapper.map(user,UserDto.class);

        return userDto;
    }

    @Override
    public UserDto findByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);

        if (user == null){
            throw new Exception("User not found");
        }
        UserDto userDto = modelMapper.map(user,UserDto.class);

        return userDto;
    }
}
