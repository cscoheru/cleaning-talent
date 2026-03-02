package com.lms.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户学习记录实体类
 */
@Data
@TableName("user_learning_records")
public class UserLearningRecord {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 报名时间
     */
    private LocalDateTime enrollmentDate;

    /**
     * 已完成课时数
     */
    private Integer completedLessons;

    /**
     * 总课时数
     */
    private Integer totalLessons;

    /**
     * 进度百分比
     */
    private BigDecimal progressPercentage;

    /**
     * 状态：NOT_STARTED, IN_PROGRESS, COMPLETED
     */
    private String status;

    /**
     * 完成时间
     */
    private LocalDateTime completedAt;

    /**
     * 最后访问时间
     */
    private LocalDateTime lastAccessAt;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 课程信息（非数据库字段）
     */
    @TableField(exist = false)
    private Course course;
}
