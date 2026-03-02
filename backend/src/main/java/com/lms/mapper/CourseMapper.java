package com.lms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lms.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 课程 Mapper
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    /**
     * 分页查询课程（带分类信息）
     */
    @Select("<script>" +
            "SELECT c.*, cat.name as category_name, cat.description as category_description " +
            "FROM courses c " +
            "LEFT JOIN categories cat ON c.category_id = cat.id " +
            "WHERE 1=1 " +
            "<if test='categoryId != null'>AND c.category_id = #{categoryId}</if> " +
            "<if test='difficulty != null'>AND c.difficulty = #{difficulty}</if> " +
            "<if test='status != null'>AND c.status = #{status}</if> " +
            "ORDER BY c.created_at DESC " +
            "</script>")
    Page<Course> selectCoursePageWithCategory(
            Page<Course> page,
            @Param("categoryId") Long categoryId,
            @Param("difficulty") String difficulty,
            @Param("status") String status
    );
}
