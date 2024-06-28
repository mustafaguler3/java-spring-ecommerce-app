package com.example.thymeleaf_demo.config;

import com.example.thymeleaf_demo.filter.CustomAuthenticationProvider;
import com.example.thymeleaf_demo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String[] publicUrl = {"/",
            "/register/**",
            "/register",
            "/products",
            "/forgot-password",
            "/reset-password",
            "/reset-password/**",

            "/login",
            "/login/**",
            "/verify",
            "/images/**",
            "/uploads/**",
            "/src/**",
            "/webjars/**",
            "/resources/**",
            "/assets/**",
            "/css/**",
            "/summernote/**",
            "/js/**",
            "/*.css",
            "/*.js",
            "/*.js.map",
            "/fonts**", "/favicon.ico", "/resources/**", "/error"};



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> {
            csrf.disable();
        });
        http.cors(cors -> cors.disable());

        http.authorizeHttpRequests(auth ->
                auth.requestMatchers(publicUrl)
                        .permitAll()
                        .requestMatchers("/profile/**").authenticated()
                        .anyRequest().authenticated());

        //http.authenticationProvider(new CustomAuthenticationProvider());

        http.formLogin(formLogin ->
                formLogin.loginPage("/login")
                        .failureUrl("/login?error")
                       .defaultSuccessUrl("/home", true)
                        .permitAll())
                .logout(logout ->
                        logout.permitAll()
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login?logout")
                                .invalidateHttpSession(true)// Oturumun geçerliliğini sonlandır
                                .deleteCookies("JSESSIONID"));

        //http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }
    /*@Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    } */
}




























