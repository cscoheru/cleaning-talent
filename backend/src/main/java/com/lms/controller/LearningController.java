package com.lms.controller;

import com.lms.dto.*;
import com.lms.service.LearningService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 学习控制器
 */
@RestController
@RequestMapping("/api/v1/learning")
public class LearningController {

    @Autowired
    private LearningService learningService;

    /**
     * 获取学习进度
     * GET /api/v1/learning/progress?courseId=1
     */
    @GetMapping("/progress")
    public ApiResponse<LearningProgressResponse> getLearningProgress(
            @RequestParam(required = false) Long courseId
    ) {
        Long userId = learningService.getCurrentUserId();
        if (courseId == null) {
            return ApiResponse.error(400, "课程ID不能为空");
        }
        LearningProgressResponse response = learningService.getLearningProgress(userId, courseId);
        return ApiResponse.success(response);
    }

    /**
     * 记录学习行为
     * POST /api/v1/learning/record
     */
    @PostMapping("/record")
    public ApiResponse<Void> recordLearning(@Valid @RequestBody RecordLearningRequest request) {
        Long userId = learningService.getCurrentUserId();
        learningService.recordLearning(userId, request);
        return ApiResponse.success("学习记录已保存", null);
    }

    /**
     * 完成课程
     * POST /api/v1/learning/complete
     */
    @PostMapping("/complete")
    public ApiResponse<CompleteCourseResponse> completeCourse(@Valid @RequestBody CompleteCourseRequest request) {
        Long userId = learningService.getCurrentUserId();
        CompleteCourseResponse response = learningService.completeCourse(userId, request);
        return ApiResponse.success(response);
    }

    /**
     * 获取学习历史
     * GET /api/v1/learning/history?page=1&size=20
     */
    @GetMapping("/history")
    public ApiResponse<PageResponse<LearningProgressResponse>> getLearningHistory(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        Long userId = learningService.getCurrentUserId();
        PageResponse<LearningProgressResponse> response = learningService.getLearningHistory(userId, page, size);
        return ApiResponse.success(response);
    }
}
