package com.example.backform.auth;

import com.example.backform.auth.dto.LoginRequest;
import com.example.backform.auth.dto.LoginResponse;
import com.example.backform.auth.dto.RegisterRequest;
import com.example.backform.auth.dto.UserBasicResponse;
import com.example.backform.common.BusinessException;
import com.example.backform.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final UserMapper userMapper;
    private final CaptchaService captchaService;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserMapper userMapper, CaptchaService captchaService, TokenService tokenService) {
        this.userMapper = userMapper;
        this.captchaService = captchaService;
        this.tokenService = tokenService;
    }

    public UserBasicResponse register(RegisterRequest request) {
        validateRegisterRequest(request);
        captchaService.verify(request.getCaptchaKey(), request.getCaptchaCode(), "register");
        if (userMapper.countByUsername(request.getUsername()) > 0) {
            throw new BusinessException(409, "用户名已存在", HttpStatus.CONFLICT);
        }
        if (userMapper.countByEmail(request.getEmail()) > 0) {
            throw new BusinessException(409, "邮箱已存在", HttpStatus.CONFLICT);
        }
        userMapper.insertUser(request.getUsername(), request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getNickname());
        Map<String, Object> createdUser = userMapper.findByAccount(request.getUsername());
        return buildUserBasicResponse(createdUser);
    }

    public LoginResponse login(LoginRequest request) {
        captchaService.verify(request.getCaptchaKey(), request.getCaptchaCode(), "login");
        Map<String, Object> userRow = userMapper.findByAccount(request.getUsername());
        if (userRow == null || !passwordEncoder.matches(request.getPassword(), String.valueOf(userRow.get("password_hash")))) {
            throw new BusinessException(401, "用户名或密码错误", HttpStatus.UNAUTHORIZED);
        }
        if (!"active".equals(userRow.get("status"))) {
            throw new BusinessException(403, "该账号已被禁用", HttpStatus.FORBIDDEN);
        }
        CurrentUser currentUser = new CurrentUser(
                ((Number) userRow.get("id")).longValue(),
                String.valueOf(userRow.get("username")),
                String.valueOf(userRow.get("nickname")),
                String.valueOf(userRow.get("email")),
                String.valueOf(userRow.get("role")),
                String.valueOf(userRow.get("avatar_url"))
        );
        userMapper.updateLastLogin(currentUser.id());
        String token = tokenService.issue(currentUser);
        return new LoginResponse(token, buildUserBasicResponse(userRow));
    }

    private void validateRegisterRequest(RegisterRequest request) {
        validatePasswordConfirmation(request.getPassword(), request.getConfirmPassword());
    }

    private void validatePasswordConfirmation(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new BusinessException(400, "两次密码不一致", HttpStatus.BAD_REQUEST);
        }
    }

    private UserBasicResponse buildUserBasicResponse(Map<String, Object> userRow) {
        return new UserBasicResponse(
                ((Number) userRow.get("id")).longValue(),
                String.valueOf(userRow.get("username")),
                String.valueOf(userRow.get("nickname")),
                String.valueOf(userRow.get("email")),
                String.valueOf(userRow.get("role")),
                String.valueOf(userRow.get("avatar_url"))
        );
    }
}
