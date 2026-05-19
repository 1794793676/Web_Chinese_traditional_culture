package com.example.backform.user;

import com.example.backform.auth.CurrentUser;
import com.example.backform.common.ApiResponse;
import com.example.backform.user.dto.UserProfileResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ApiResponse<UserProfileResponse> profile(HttpServletRequest request) {
        CurrentUser currentUser = (CurrentUser) request.getAttribute("currentUser");
        return ApiResponse.ok(userService.getProfile(currentUser));
    }
}
