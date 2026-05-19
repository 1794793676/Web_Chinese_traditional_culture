package com.example.backform.auth.dto;

public record CaptchaResponse(String captchaKey, String captchaImage, String expireAt) {
}
