package com.example.thymeleaf_demo;

import com.example.thymeleaf_demo.domain.Category;
import com.example.thymeleaf_demo.domain.Product;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.repository.CategoryRepository;
import com.example.thymeleaf_demo.repository.ProductRepository;
import com.example.thymeleaf_demo.service.CategoryService;
import com.example.thymeleaf_demo.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
@EntityScan("com.example.thymeleaf_demo.domain")
@EnableJpaRepositories("com.example.thymeleaf_demo.repository")
//@EnableWebSecurity(debug = true)
public class ThymeleafDemoApplication{

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CategoryRepository categoryRepository;

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
	//mail sender

	/*@Override
	public void run(String... args) throws Exception {

		List<Category> category = Arrays.asList(
				new Category("Computer"),
				new Category("Phone"),
				new Category("Tablet"),
				new Category("Monitor"),
                new Category("Mouse"),
                new Category("Keyboard"),
                new Category("Headphones"),
				new Category("Television")
		);
		categoryRepository.saveAll(category);

		Category computer = categoryRepository.findByName("Computer");
		Category phone = categoryRepository.findByName("Phone");
		Category tablet = categoryRepository.findByName("Tablet");

		List<Product> products = Arrays.asList(
				new Product("Abra A5","Abra A5 V20.4.4 Intel Core i5 12450H 16 GB RAM 1 TB SSD 6 GB RTX 4050 FreeDos 15,6\" FHD 144 Hz",new BigDecimal(2500),"7.webp",computer,"Monster",120),
				new Product("Excalibur G770","Excalibur G770.1245-BVJ0X-B Intel Core İ5-12450H 16GB RAM 500GB NVME SSD Gen4 4GB RTX3050 Freedos",new BigDecimal(3000),"9.webp",computer,"Casper",50),
				new Product("Macbook Air M1","Macbook Air M1 Çip 8gb 256gb Ssd Macos 13\" Qhd Taşınabilir Bilgisayar Uzay Grisi",new BigDecimal(4500),"10.webp",computer,"Apple",25),
				new Product("Macbook Air M2","Macbook Air 13.6\" M2 8c Gpu 8 Gb 256 Gb Ssd Yıldız Işığı",new BigDecimal(4200),"11.webp",computer,"Apple",20),
				new Product("Iphone 12","iPhone 12 128 GB Mor Cep Telefonu Aksesuarsız Kutu",new BigDecimal(2500),"12.webp",phone,"Apple",24),
				new Product("Iphone 13","iPhone 13 128 GB Yıldız Işığı Cep Telefonu",new BigDecimal(3000),"13.webp",phone,"Apple",15),
				new Product("Redmi 12","Redmi 12 128 GB 8 GB RAM Siyah Cep Telefonu",new BigDecimal(3270),"14.webp",phone,"Xiaomi",90),
				new Product("Galaxy Tab A9","Galaxy Tab A9 8 GB RAM 128 GB Grafit",new BigDecimal(4350),"15.webp",tablet,"Samsung",18)
		);

		productRepository.saveAll(products);
	}*/
}
