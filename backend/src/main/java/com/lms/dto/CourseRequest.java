package com.lms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 课程请求 DTO
 */
@Data
public class CourseRequest {

    @NotBlank(message = "课程标题不能为空")
    private String title;

    private Long categoryId;

    private String description;

    private String coverUrl;

    @NotNull(message = "课程时长不能为空")
    private Integer duration;

    private String difficulty;

    private Integer pointsReward;

    private String status;
}
