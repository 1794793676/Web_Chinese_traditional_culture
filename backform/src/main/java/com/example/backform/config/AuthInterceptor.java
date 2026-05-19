package com.example.backform.config;
import com.example.backform.auth.CurrentUser;
import com.example.backform.auth.TokenService;
import com.example.backform.common.BusinessException;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component
public class AuthInterceptor implements HandlerInterceptor {
 private final TokenService t; public AuthInterceptor(TokenService t){this.t=t;}
 public boolean preHandle(HttpServletRequest req,HttpServletResponse res,Object handler){ String p=req.getRequestURI(); if(p.equals("/api/auth/register")||p.equals("/api/auth/login")||p.equals("/api/auth/captcha")||p.equals("/api/categories")||p.equals("/api/articles/featured")) return true; if(!p.startsWith("/api/")) return true; String h=req.getHeader("Authorization"); if(h==null||!h.startsWith("Bearer ")) throw new BusinessException(401,"请先登录"); CurrentUser u=t.verify(h.substring(7)); if(u==null) throw new BusinessException(401,"请先登录"); req.setAttribute("currentUser",u); if(p.startsWith("/api/admin/")&&!"admin".equals(u.role())) throw new BusinessException(403,"无管理员权限"); return true; }
}
