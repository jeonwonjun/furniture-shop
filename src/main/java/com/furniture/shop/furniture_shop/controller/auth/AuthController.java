package com.furniture.shop.furniture_shop.controller.auth;

import com.furniture.shop.furniture_shop.config.jwt.JwtTokenProvider;
import com.furniture.shop.furniture_shop.controller.auth.dto.AuthResponse;
import com.furniture.shop.furniture_shop.controller.auth.dto.LoginRequest;
import com.furniture.shop.furniture_shop.controller.auth.dto.SignupRequest;
import com.furniture.shop.furniture_shop.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest req){
        authService.signup(req);
        return ResponseEntity.noContent().build(); // 204
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req){
        Authentication auth = authService.authentication(req);
        String token = tokenProvider.generateToken(auth.getName());
        return ResponseEntity.ok(new AuthResponse(token));
    }

}
