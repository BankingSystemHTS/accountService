package com.example.account_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
   
   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
         .authorizeHttpRequests((requests) -> requests
            .requestMatchers("/api/user/**").permitAll() //reuqire authentication for /api/user/**   
            .anyRequest().permitAll() //permit all other requests
            );
         
         
      return http.build();

   }
}
