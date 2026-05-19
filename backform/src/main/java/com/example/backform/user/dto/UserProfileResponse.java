package com.example.backform.user.dto;

public record UserProfileResponse(
        Long id,
        String username,
        String nickname,
        String email,
        String role,
        String avatarUrl,
        String createdAt,
        String lastLoginAt
) {
}
