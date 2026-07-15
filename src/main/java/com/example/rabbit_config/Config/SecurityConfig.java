package com.example.rabbit_config.Config;

import com.example.rabbit_config.OAuth.OAuthSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final OAuthSuccessHandler successHandler;

    public SecurityConfig(OAuthSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/api/v1/webhook/incoming-email").permitAll()
                        .anyRequest().authenticated())

                .oauth2Login(oauth -> oauth
                        .successHandler(successHandler));

        return http.build();
    }
}