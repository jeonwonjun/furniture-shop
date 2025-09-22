package com.furniture.shop.furniture_shop.service;

import com.furniture.shop.furniture_shop.controller.auth.dto.LoginRequest;
import com.furniture.shop.furniture_shop.controller.auth.dto.SignupRequest;
import com.furniture.shop.furniture_shop.model.user.Role;
import com.furniture.shop.furniture_shop.model.user.User;
import com.furniture.shop.furniture_shop.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public void signup(SignupRequest signupRequest){
        if(userRepository.existsByEmail(signupRequest.email())){
            throw new IllegalArgumentException("Email already exists");
        }
        User user = User.builder()
                .email(signupRequest.email())
                .password(passwordEncoder.encode(signupRequest.password()))
                .role(Role.USER)
                .enabled(true)
                .build();
        userRepository.save(user);
    }

    public Authentication authentication(LoginRequest loginRequest){
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );
    }
}
