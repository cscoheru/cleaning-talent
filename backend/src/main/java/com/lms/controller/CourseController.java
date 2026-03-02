package com.lms.controller;

import com.lms.dto.CourseRequest;
import com.lms.dto.CourseResponse;
import com.lms.dto.CourseDetailResponse;
import com.lms.dto.PageResponse;
import com.lms.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 课程控制器
 */
@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 获取课程列表
     * GET /api/v1/courses?page=1&size=20&category=1&difficulty=BEGINNER&status=PUBLISHED
     */
    @GetMapping
    public ApiResponse<PageResponse<CourseResponse>> getCourseList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String status
    ) {
        PageResponse<CourseResponse> response = courseService.getCourseList(page, size, category, difficulty, status);
        return ApiResponse.success(response);
    }

    /**
     * 获取课程详情
     * GET /api/v1/courses/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<CourseDetailResponse> getCourseDetail(@PathVariable Long id) {
        CourseDetailResponse response = courseService.getCourseDetail(id);
        return ApiResponse.success(response);
    }

    /**
     * 创建课程（需要 ADMIN 权限）
     * POST /api/v1/courses
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CourseResponse> createCourse(@Valid @RequestBody CourseRequest request) {
        // 获取当前用户ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        CourseResponse response = courseService.createCourse(request, userId);
        return ApiResponse.success(response);
    }

    /**
     * 更新课程（需要 ADMIN 权限）
     * PUT /api/v1/courses/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CourseResponse> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequest request
    ) {
        CourseResponse response = courseService.updateCourse(id, request);
        return ApiResponse.success(response);
    }

    /**
     * 删除课程（需要 ADMIN 权限）
     * DELETE /api/v1/courses/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ApiResponse.success("课程删除成功", null);
    }
}
