package com.lms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 完成课程请求 DTO
 */
@Data
public class CompleteCourseRequest {

    @NotNull(message = "课程ID不能为空")
    private Long courseId;
}
