package com.example.backform.user;
import com.example.backform.auth.CurrentUser;
import com.example.backform.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/user")
public class UserController {
 private final UserRepository r; public UserController(UserRepository r){this.r=r;}
 @GetMapping("/profile") public ApiResponse<CurrentUser> p(HttpServletRequest req){ CurrentUser u=(CurrentUser)req.getAttribute("currentUser"); return ApiResponse.ok(r.byId(u.id())); }
}
