package com.smartshop.user.controller;

import com.smartshop.user.dto.*;
import com.smartshop.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(
                authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(
                authService.login(request));
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validate(
            @RequestHeader("Authorization") String token) {
        String email = authService.validateToken(
                token.substring(7));
        return ResponseEntity.ok(email);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("User Service UP");
    }
}