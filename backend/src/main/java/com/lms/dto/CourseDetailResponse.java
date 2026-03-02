package com.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 课程详情响应 DTO（包含章节）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetailResponse {

    private Long id;
    private String title;
    private String description;
    private String coverUrl;
    private Integer duration;
    private String difficulty;
    private Integer pointsReward;
    private String status;
    private Long categoryId;
    private CategoryInfo category;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ChapterResponse> chapters;
}
