package com.furniture.shop.furniture_shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // 개발 단계에서는 꺼도 됨
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/hello").permitAll() // 특정 API 허용
                        .anyRequest().authenticated()              // 나머지는 인증 필요
                )
                .formLogin().disable()   // /login 폼 비활성화
                .httpBasic();            // 필요하다면 HTTP Basic 활성화

        return http.build();
    }
}

