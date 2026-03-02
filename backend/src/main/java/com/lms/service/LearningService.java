package com.lms.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lms.dto.*;
import com.lms.entity.*;
import com.lms.exception.BusinessException;
import com.lms.exception.ResourceNotFoundException;
import com.lms.mapper.*;
import com.lms.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * 学习服务
 */
@Service
public class LearningService {

    @Autowired
    private UserLearningRecordMapper learningRecordMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PointsRecordMapper pointsRecordMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    /**
     * 获取用户学习进度
     */
    public LearningProgressResponse getLearningProgress(Long userId, Long courseId) {
        UserLearningRecord record = learningRecordMapper.selectOne(
                new LambdaQueryWrapper<UserLearningRecord>()
                        .eq(UserLearningRecord::getUserId, userId)
                        .eq(UserLearningRecord::getCourseId, courseId)
        );

        if (record == null) {
            throw new ResourceNotFoundException("学习记录不存在");
        }

        // 获取课程标题
        Course course = courseMapper.selectById(courseId);

        return LearningProgressResponse.builder()
                .id(record.getId())
                .courseId(record.getCourseId())
                .courseTitle(course != null ? course.getTitle() : "")
                .completedLessons(record.getCompletedLessons())
                .totalLessons(record.getTotalLessons())
                .progressPercentage(record.getProgressPercentage())
                .status(record.getStatus())
                .enrollmentDate(record.getEnrollmentDate())
                .lastAccessAt(record.getLastAccessAt())
                .completedAt(record.getCompletedAt())
                .build();
    }

    /**
     * 记录学习行为
     */
    @Transactional
    public void recordLearning(Long userId, RecordLearningRequest request) {
        // 验证课程是否存在
        Course course = courseMapper.selectById(request.getCourseId());
        if (course == null) {
            throw new ResourceNotFoundException("Course", request.getCourseId());
        }

        // 查找或创建学习记录
        UserLearningRecord record = learningRecordMapper.selectOne(
                new LambdaQueryWrapper<UserLearningRecord>()
                        .eq(UserLearningRecord::getUserId, userId)
                        .eq(UserLearningRecord::getCourseId, request.getCourseId())
        );

        if (record == null) {
            // 创建新的学习记录
            record = new UserLearningRecord();
            record.setUserId(userId);
            record.setCourseId(request.getCourseId());
            record.setEnrollmentDate(LocalDateTime.now());
            record.setCompletedLessons(request.getCompletedLessons() != null ? request.getCompletedLessons() : 0);
            record.setTotalLessons(request.getTotalLessons() != null ? request.getTotalLessons() : getTotalChapterCount(request.getCourseId()));
            record.setProgressPercentage(calculateProgress(record.getCompletedLessons(), record.getTotalLessons()));
            record.setStatus("NOT_STARTED");
            record.setLastAccessAt(LocalDateTime.now());
            learningRecordMapper.insert(record);
        } else {
            // 更新学习记录
            if (request.getCompletedLessons() != null) {
                record.setCompletedLessons(request.getCompletedLessons());
            }
            if (request.getTotalLessons() != null) {
                record.setTotalLessons(request.getTotalLessons());
            }
            record.setProgressPercentage(calculateProgress(record.getCompletedLessons(), record.getTotalLessons()));
            record.setLastAccessAt(LocalDateTime.now());

            // 更新状态
            if (record.getProgressPercentage().compareTo(BigDecimal.valueOf(100)) >= 0) {
                record.setStatus("COMPLETED");
                record.setCompletedAt(LocalDateTime.now());
            } else if (record.getProgressPercentage().compareTo(BigDecimal.ZERO) > 0) {
                record.setStatus("IN_PROGRESS");
            }

            learningRecordMapper.updateById(record);
        }
    }

    /**
     * 完成课程
     */
    @Transactional
    public CompleteCourseResponse completeCourse(Long userId, CompleteCourseRequest request) {
        // 验证课程是否存在
        Course course = courseMapper.selectById(request.getCourseId());
        if (course == null) {
            throw new ResourceNotFoundException("Course", request.getCourseId());
        }

        // 查找学习记录
        UserLearningRecord record = learningRecordMapper.selectOne(
                new LambdaQueryWrapper<UserLearningRecord>()
                        .eq(UserLearningRecord::getUserId, userId)
                        .eq(UserLearningRecord::getCourseId, request.getCourseId())
        );

        if (record == null) {
            throw new BusinessException(3002, "学习记录不存在，请先开始学习");
        }

        if ("COMPLETED".equals(record.getStatus())) {
            throw new BusinessException(409, "课程已完成，无需重复完成");
        }

        // 更新学习记录为完成状态
        record.setStatus("COMPLETED");
        record.setCompletedAt(LocalDateTime.now());
        record.setProgressPercentage(BigDecimal.valueOf(100));
        record.setCompletedLessons(record.getTotalLessons());
        learningRecordMapper.updateById(record);

        // 获取课程奖励积分
        Integer pointsReward = course.getPointsReward() != null ? course.getPointsReward() : 100;

        // 增加用户积分
        User user = userMapper.selectById(userId);
        if (user != null) {
            user.setPoints(user.getPoints() + pointsReward);
            userMapper.updateById(user);

            // 记录积分变动
            PointsRecord pointsRecord = new PointsRecord();
            pointsRecord.setUserId(userId);
            pointsRecord.setPoints(pointsReward);
            pointsRecord.setType("COURSE_COMPLETE");
            pointsRecord.setDescription("完成《" + course.getTitle() + "》课程");
            pointsRecord.setRelatedId(course.getId());
            pointsRecordMapper.insert(pointsRecord);
        }

        return CompleteCourseResponse.builder()
                .message("课程完成，获得 " + pointsReward + " 积分")
                .pointsEarned(pointsReward)
                .build();
    }

    /**
     * 获取学习历史
     */
    public PageResponse<LearningProgressResponse> getLearningHistory(Long userId, Integer page, Integer size) {
        Page<UserLearningRecord> pageParam = new Page<>(page, size);

        Page<UserLearningRecord> resultPage = learningRecordMapper.selectPage(
                pageParam,
                new LambdaQueryWrapper<UserLearningRecord>()
                        .eq(UserLearningRecord::getUserId, userId)
                        .orderByDesc(UserLearningRecord::getLastAccessAt)
        );

        List<LearningProgressResponse> responses = resultPage.getRecords().stream()
                .map(record -> {
                    Course course = courseMapper.selectById(record.getCourseId());
                    return LearningProgressResponse.builder()
                            .id(record.getId())
                            .courseId(record.getCourseId())
                            .courseTitle(course != null ? course.getTitle() : "")
                            .completedLessons(record.getCompletedLessons())
                            .totalLessons(record.getTotalLessons())
                            .progressPercentage(record.getProgressPercentage())
                            .status(record.getStatus())
                            .enrollmentDate(record.getEnrollmentDate())
                            .lastAccessAt(record.getLastAccessAt())
                            .completedAt(record.getCompletedAt())
                            .build();
                })
                .collect(java.util.stream.Collectors.toList());

        Page<UserLearningRecord> responsePage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        responsePage.setRecords(responses);

        return PageResponse.of(responsePage);
    }

    /**
     * 计算进度百分比
     */
    private BigDecimal calculateProgress(Integer completed, Integer total) {
        if (total == null || total == 0) {
            return BigDecimal.ZERO;
        }
        if (completed == null) {
            completed = 0;
        }
        return BigDecimal.valueOf(completed)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
    }

    /**
     * 获取课程总章节数
     */
    private Integer getTotalChapterCount(Long courseId) {
        return chapterMapper.selectCount(
                new LambdaQueryWrapper<Chapter>().eq(Chapter::getCourseId, courseId)
        ).intValue();
    }

    /**
     * 获取当前登录用户ID
     */
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException(ApiResponse.ErrorCode.UNAUTHORIZED);
        }
        return (Long) authentication.getPrincipal();
    }
}
