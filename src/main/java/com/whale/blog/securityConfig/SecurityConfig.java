package com.whale.blog.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/posts", "/users/login", "/users/signup", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/posts/**").authenticated()
                        .anyRequest().authenticated()
                )
                // 1. formLogin 설정
                .formLogin(login -> login
                        .loginPage("/users/login")    // ★주의: 위 requestMatchers에 적은 주소와 맞춰주세요!
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/posts", true)
                        .permitAll()
                ) // <--- 여기서 formLogin 괄호를 닫아줘야 합니다!

                // 2. exceptionHandling 설정 (formLogin 밖으로 빼냄)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/users/login?needLogin=true"))
                );

        return http.build();
    }
}