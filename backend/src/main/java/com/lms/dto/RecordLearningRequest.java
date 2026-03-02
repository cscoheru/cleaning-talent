package com.lms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 记录学习请求 DTO
 */
@Data
public class RecordLearningRequest {

    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    private Integer duration;

    private Integer completedLessons;
}
