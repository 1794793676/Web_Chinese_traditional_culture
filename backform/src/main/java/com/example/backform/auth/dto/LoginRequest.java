package com.example.backform.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {
    @NotBlank(message = "账号不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 64, message = "密码长度必须在 8-64 之间")
    private String password;

    @NotBlank(message = "captchaKey 不能为空")
    private String captchaKey;

    @NotBlank(message = "captchaCode 不能为空")
    private String captchaCode;
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCaptchaKey() { return captchaKey; }
    public void setCaptchaKey(String captchaKey) { this.captchaKey = captchaKey; }
    public String getCaptchaCode() { return captchaCode; }
    public void setCaptchaCode(String captchaCode) { this.captchaCode = captchaCode; }
}
