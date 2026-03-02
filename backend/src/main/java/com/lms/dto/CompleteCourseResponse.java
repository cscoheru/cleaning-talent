package com.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 完成课程响应 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompleteCourseResponse {

    /**
     * 响应消息
     */
    private String message;

    /**
     * 获得的积分
     */
    private Integer pointsEarned;
}
