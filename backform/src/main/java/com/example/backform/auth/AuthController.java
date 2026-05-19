package com.example.backform.auth;
import com.example.backform.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/auth")
public class AuthController {
  private final AuthService s; private final CaptchaService c; private final TokenService t;
  public AuthController(AuthService s,CaptchaService c,TokenService t){this.s=s;this.c=c;this.t=t;}
  @GetMapping("/captcha") public ApiResponse<Map<String,Object>> captcha(@RequestParam(defaultValue="login") String purpose){ return ApiResponse.ok(c.create(purpose)); }
  @PostMapping("/register") public ApiResponse<Map<String,Object>> register(@RequestBody Map<String,String> req){ return ApiResponse.ok(s.register(req)); }
  @PostMapping("/login") public ApiResponse<Map<String,Object>> login(@RequestBody Map<String,String> req){ return ApiResponse.ok(s.login(req)); }
  @PostMapping("/logout") public ApiResponse<Void> logout(HttpServletRequest req){ String h=req.getHeader("Authorization"); if(h!=null&&h.startsWith("Bearer ")) t.revoke(h.substring(7)); return ApiResponse.ok(null);} }
