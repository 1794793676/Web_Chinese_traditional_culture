package com.example.backform.auth;

import com.example.backform.auth.dto.CaptchaResponse;
import com.example.backform.auth.dto.LoginRequest;
import com.example.backform.auth.dto.LoginResponse;
import com.example.backform.auth.dto.RegisterRequest;
import com.example.backform.auth.dto.UserBasicResponse;
import com.example.backform.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final CaptchaService captchaService;
    private final TokenService tokenService;

    public AuthController(AuthService authService, CaptchaService captchaService, TokenService tokenService) {
        this.authService = authService;
        this.captchaService = captchaService;
        this.tokenService = tokenService;
    }

    @GetMapping("/captcha")
    public ApiResponse<CaptchaResponse> captcha(@RequestParam(defaultValue = "login") String purpose) {
        return ApiResponse.ok(captchaService.create(purpose));
    }

    @PostMapping("/register")
    public ApiResponse<UserBasicResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            tokenService.revoke(header.substring(7));
        }
        return ApiResponse.ok(null);
    }
}
