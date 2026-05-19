package com.example.backform.auth.dto;

public record LoginResponse(String token, UserBasicResponse user) {
}
