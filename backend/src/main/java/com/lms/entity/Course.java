package com.lms.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 课程实体类
 */
@Data
@TableName("courses")
public class Course {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程标题
     */
    private String title;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 课程描述
     */
    private String description;

    /**
     * 封面图URL
     */
    private String coverUrl;

    /**
     * 时长（分钟）
     */
    private Integer duration;

    /**
     * 难度：BEGINNER, INTERMEDIATE, ADVANCED
     */
    private String difficulty;

    /**
     * 完成奖励积分
     */
    private Integer pointsReward;

    /**
     * 状态：DRAFT, PUBLISHED, ARCHIVED
     */
    private String status;

    /**
     * 创建者ID
     */
    private Long createdBy;

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
     * 分类信息（非数据库字段）
     */
    @TableField(exist = false)
    private Category category;
}
