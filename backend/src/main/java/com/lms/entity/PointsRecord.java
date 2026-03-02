package com.lms.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分记录实体类
 */
@Data
@TableName("points_records")
public class PointsRecord {

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
     * 积分变动（正/负）
     */
    private Integer points;

    /**
     * 类型：COURSE_COMPLETE, DAILY_LOGIN, QUIZ_PASS
     */
    private String type;

    /**
     * 描述
     */
    private String description;

    /**
     * 关联ID（课程ID等）
     */
    private Long relatedId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
