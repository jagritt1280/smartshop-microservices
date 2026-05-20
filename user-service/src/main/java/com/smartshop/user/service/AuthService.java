package com.smartshop.user.service;

import com.smartshop.user.dto.*;
import com.smartshop.user.entity.User;
import com.smartshop.user.repository.UserRepository;
import com.smartshop.user.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email already exists");

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(
                        request.getPassword()))
                .role(User.Role.USER)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(
                user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .id(user.getId())
                .token(token)
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .message("Registration successful")
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid credentials"));

        if(!passwordEncoder.matches(
                request.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid credentials");

        String token = jwtUtil.generateToken(
                user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .id(user.getId())
                .token(token)
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .message("Login successful")
                .build();
    }

    public String validateToken(String token) {
        if(jwtUtil.isValid(token))
            return jwtUtil.extractEmail(token);
        throw new RuntimeException("Invalid token");
    }
}