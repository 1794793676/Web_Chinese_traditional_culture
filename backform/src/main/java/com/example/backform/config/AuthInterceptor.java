package com.example.backform.config;

import com.example.backform.auth.CurrentUser;
import com.example.backform.auth.TokenService;
import com.example.backform.common.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;

    public AuthInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String path = request.getRequestURI();
        if (!path.startsWith("/api/")) {
            return true;
        }
        if (isPublicEndpoint(request.getMethod(), path)) {
            return true;
        }

        CurrentUser currentUser = validateCurrentUser(resolveBearerToken(request));
        request.setAttribute("currentUser", currentUser);

        if (isAdminEndpoint(path) && !"admin".equals(currentUser.role())) {
            throw new BusinessException(403, "无管理员权限", HttpStatus.FORBIDDEN);
        }
        return true;
    }

    private boolean isPublicEndpoint(String method, String path) {
        return ("GET".equalsIgnoreCase(method) && "/api/auth/captcha".equals(path))
                || ("POST".equalsIgnoreCase(method) && "/api/auth/register".equals(path))
                || ("POST".equalsIgnoreCase(method) && "/api/auth/login".equals(path))
                || ("GET".equalsIgnoreCase(method) && "/api/categories".equals(path))
                || ("GET".equalsIgnoreCase(method) && "/api/articles/featured".equals(path))
                || ("POST".equalsIgnoreCase(method) && path.matches("/api/articles/\\d+/view"));
    }

    private boolean isAdminEndpoint(String path) {
        return path.startsWith("/api/admin/");
    }

    private String resolveBearerToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new BusinessException(401, "请先登录", HttpStatus.UNAUTHORIZED);
        }
        return authorizationHeader.substring(7);
    }

    private CurrentUser validateCurrentUser(String token) {
        CurrentUser currentUser = tokenService.verify(token);
        if (currentUser == null) {
            throw new BusinessException(401, "请先登录", HttpStatus.UNAUTHORIZED);
        }
        return currentUser;
    }
}
