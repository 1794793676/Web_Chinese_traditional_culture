package com.example.backform.user;

import com.example.backform.auth.CurrentUser;
import com.example.backform.common.BusinessException;
import com.example.backform.mapper.UserMapper;
import com.example.backform.user.dto.UserProfileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {
    private final UserMapper userMapper;
    public UserService(UserMapper userMapper) { this.userMapper = userMapper; }
    public UserProfileResponse getProfile(CurrentUser currentUser) {
        Map<String, Object> profile = userMapper.findProfileById(currentUser.id());
        if (profile == null) {
            throw new BusinessException(404, "用户不存在", HttpStatus.NOT_FOUND);
        }
        return new UserProfileResponse(currentUser.id(), String.valueOf(profile.get("username")), String.valueOf(profile.get("nickname")),
                String.valueOf(profile.get("email")), String.valueOf(profile.get("role")), String.valueOf(profile.get("avatar_url")),
                String.valueOf(profile.get("created_at")), String.valueOf(profile.get("last_login_at")));
    }
}
