//package ru.mixaron.apigateway.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//
//@Configuration
//@EnableWebFluxSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        return http
//                .csrf(csrf -> csrf.disable()) // Отключаем CSRF
//                .authorizeExchange(auth -> auth
//                        .pathMatchers("/auth/**").permitAll() // Разрешаем доступ к /auth/*
//                        .anyExchange().authenticated()) // Все остальные запросы требуют аутентификации
//                .build();
//    }
//}