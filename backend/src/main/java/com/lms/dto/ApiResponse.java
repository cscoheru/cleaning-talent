package com.lms.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.Instant;

/**
 * 统一API响应格式
 *
 * @param <T> 数据类型
 */
@Data
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    public ApiResponse() {
        this.timestamp = Instant.now().toEpochMilli();
    }

    public ApiResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now().toEpochMilli();
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    /**
     * 错误响应
     */
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    /**
     * 错误响应（使用业务错误码枚举）
     */
    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 业务错误码枚举
     */
    public enum ErrorCode {
        // 用户相关 (1001-1099)
        USER_NOT_FOUND(1001, "user_not_found"),
        INVALID_CREDENTIALS(1002, "invalid_credentials"),
        TOKEN_EXPIRED(1003, "token_expired"),
        TOKEN_INVALID(1004, "token_invalid"),
        USER_ALREADY_EXISTS(1005, "user_already_exists"),

        // 课程相关 (2001-2099)
        COURSE_NOT_FOUND(2001, "course_not_found"),
        COURSE_ALREADY_ENROLLED(2002, "course_already_enrolled"),
        CHAPTER_NOT_FOUND(2003, "chapter_not_found"),

        // 学习相关 (3001-3099)
        LESSON_NOT_COMPLETED(3001, "lesson_not_completed"),
        LEARNING_RECORD_NOT_FOUND(3002, "learning_record_not_found"),

        // 积分相关 (4001-4099)
        INSUFFICIENT_POINTS(4001, "insufficient_points"),

        // HTTP 标准错误码
        BAD_REQUEST(400, "bad_request"),
        UNAUTHORIZED(401, "unauthorized"),
        FORBIDDEN(403, "forbidden"),
        NOT_FOUND(404, "not_found"),
        CONFLICT(409, "conflict"),
        INTERNAL_ERROR(500, "internal_error");

        private final Integer code;
        private final String message;

        ErrorCode(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public Integer getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
