package com.example.backform.user;

import com.example.backform.auth.CurrentUser;
import com.example.backform.common.ApiResponse;
import com.example.backform.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/user")
public class UserController {
 private final UserMapper userMapper; public UserController(UserMapper userMapper){this.userMapper=userMapper;}
 @GetMapping("/profile") public ApiResponse<CurrentUser> p(HttpServletRequest req){ CurrentUser u=(CurrentUser)req.getAttribute("currentUser"); return ApiResponse.ok(userMapper.findCurrentUserById(u.id())); }
}
