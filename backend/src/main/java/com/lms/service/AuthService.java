package com.lms.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lms.dto.*;
import com.lms.entity.User;
import com.lms.exception.BusinessException;
import com.lms.exception.ResourceNotFoundException;
import com.lms.mapper.UserMapper;
import com.lms.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务
 */
@Service
public class AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 认证响应
     */
    public AuthResponse login(AuthRequest request) {
        // 查找用户
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getEmail, request.getEmail())
        );

        if (user == null) {
            throw new BusinessException(ApiResponse.ErrorCode.INVALID_CREDENTIALS);
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ApiResponse.ErrorCode.INVALID_CREDENTIALS);
        }

        // 检查用户状态
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new BusinessException(403, "用户已被禁用");
        }

        // 生成 Token
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        // 构建用户信息
        UserInfo userInfo = UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .points(user.getPoints())
                .role(user.getRole())
                .build();

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .user(userInfo)
                .build();
    }

    /**
     * 刷新 Token
     *
     * @param request 刷新 Token 请求
     * @return 新的 Token 对
     */
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        // 验证刷新 Token
        if (!jwtUtil.validateToken(request.getRefreshToken())) {
            throw new BusinessException(ApiResponse.ErrorCode.TOKEN_INVALID);
        }

        // 获取用户ID
        Long userId = jwtUtil.getUserIdFromToken(request.getRefreshToken());

        // 查找用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ApiResponse.ErrorCode.USER_NOT_FOUND);
        }

        // 检查用户状态
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new BusinessException(403, "用户已被禁用");
        }

        // 生成新 Token
        String newToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getId());

        // 构建用户信息
        UserInfo userInfo = UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .points(user.getPoints())
                .role(user.getRole())
                .build();

        return AuthResponse.builder()
                .token(newToken)
                .refreshToken(newRefreshToken)
                .user(userInfo)
                .build();
    }

    /**
     * 获取当前用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    public UserInfo getCurrentUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User", userId);
        }

        return UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .points(user.getPoints())
                .role(user.getRole())
                .build();
    }

    /**
     * 用户登出
     * Demo 阶段：前端删除 Token 即可
     * 生产环境：可将 Token 加入黑名单（Redis）
     */
    public void logout() {
        // Demo 阶段不需要实现
        // 生产环境可以将 Token 加入 Redis 黑名单
    }
}
