package com.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 学习进度响应 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningProgressResponse {

    private Long id;
    private Long courseId;
    private String courseTitle;
    private Integer completedLessons;
    private Integer totalLessons;
    private BigDecimal progressPercentage;
    private String status;
    private LocalDateTime enrollmentDate;
    private LocalDateTime lastAccessAt;
    private LocalDateTime completedAt;
}
