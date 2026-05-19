package com.example.backform.auth;

public record CurrentUser(Long id, String username, String nickname, String email, String role, String avatarUrl) {}
