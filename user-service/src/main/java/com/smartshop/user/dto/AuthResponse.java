package com.smartshop.user.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private Long id;        // ADD THIS ✅
    private String token;
    private String name;
    private String email;
    private String role;
    private String message;
}