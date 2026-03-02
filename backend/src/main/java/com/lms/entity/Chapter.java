package com.lms.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 课程章节实体类
 */
@Data
@TableName("chapters")
public class Chapter {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 章节标题
     */
    private String title;

    /**
     * 章节描述
     */
    private String description;

    /**
     * 视频URL
     */
    private String videoUrl;

    /**
     * 时长（秒）
     */
    private Integer duration;

    /**
     * 排序
     */
    private Integer orderIndex;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
