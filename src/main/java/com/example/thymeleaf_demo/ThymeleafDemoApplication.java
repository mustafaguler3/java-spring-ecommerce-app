package com.example.thymeleaf_demo;

import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Properties;

@SpringBootApplication
@EntityScan("com.example.thymeleaf_demo.domain")
@EnableJpaRepositories("com.example.thymeleaf_demo.repository")
//@EnableWebSecurity(debug = true)
public class ThymeleafDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThymeleafDemoApplication.class, args);
	}


	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ModelMapper modelMapper(){

		ModelMapper modelMapper = new ModelMapper();

		modelMapper.typeMap(User.class , UserDto.class)
				.addMappings(mapper -> {
					mapper.map(User::getProfilePicture,UserDto::setProfilePicture);
				});

		modelMapper.typeMap(UserDto.class,User.class)
				.addMappings(mapper -> {
					mapper.map(UserDto::getProfilePicture,User::setProfilePicture);
				});


		return modelMapper;
	}
	@Bean
	public JavaMailSender mailSender(){
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);

		mailSender.setUsername("mustafaguler4@gmail.com");
		mailSender.setPassword("dlkm bzsl muyl yhdb");

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}
}
