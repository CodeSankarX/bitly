package com.project.bitly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
          .authorizeHttpRequests(auth -> auth
                  .requestMatchers("/public/**").permitAll()
              .anyRequest().authenticated()
          )
          .oauth2Login(oauth2 -> oauth2
              .successHandler((request, response, authentication) -> {
                  DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
                  String username = user.getAttribute("login");
                  System.out.println("Logged in GitHub user: " + username);
              })
          );

        return http.build();
    }
}