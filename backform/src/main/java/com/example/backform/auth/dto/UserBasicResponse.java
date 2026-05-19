package com.example.backform.auth.dto;

public record UserBasicResponse(Long id, String username, String nickname, String email, String role, String avatarUrl) {
}
