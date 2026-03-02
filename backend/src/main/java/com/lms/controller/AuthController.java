package com.lms.controller;

import com.lms.dto.*;
import com.lms.service.AuthService;
import com.lms.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户登录
     * POST /api/v1/auth/login
     */
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ApiResponse.success(response);
    }

    /**
     * 用户登出
     * POST /api/v1/auth/logout
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        authService.logout();
        return ApiResponse.success("登出成功", null);
    }

    /**
     * 刷新 Token
     * POST /api/v1/auth/refresh
     */
    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return ApiResponse.success(response);
    }

    /**
     * 获取当前用户信息
     * GET /api/v1/auth/me
     */
    @GetMapping("/me")
    public ApiResponse<UserInfo> getCurrentUser() {
        // 从 Security Context 获取当前用户ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ApiResponse.error(ApiResponse.ErrorCode.UNAUTHORIZED);
        }

        Long userId = (Long) authentication.getPrincipal();
        UserInfo userInfo = authService.getCurrentUser(userId);
        return ApiResponse.success(userInfo);
    }
}
