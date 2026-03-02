package com.lms.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lms.dto.*;
import com.lms.entity.Category;
import com.lms.entity.Chapter;
import com.lms.entity.Course;
import com.lms.exception.ResourceNotFoundException;
import com.lms.mapper.CategoryMapper;
import com.lms.mapper.ChapterMapper;
import com.lms.mapper.CourseMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 课程服务
 */
@Service
public class CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    /**
     * 分页查询课程列表
     */
    public PageResponse<CourseResponse> getCourseList(Integer page, Integer size, Long categoryId, String difficulty, String status) {
        // 创建分页对象
        Page<Course> pageParam = new Page<>(page, size);

        // 执行分页查询
        Page<Course> resultPage = courseMapper.selectCoursePageWithCategory(pageParam, categoryId, difficulty, status);

        // 转换为 DTO
        List<CourseResponse> courseResponses = resultPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        // 构建分页响应
        Page<Course> responsePage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        responsePage.setRecords(courseResponses);

        return PageResponse.of(responsePage);
    }

    /**
     * 获取课程详情（包含章节）
     */
    public CourseDetailResponse getCourseDetail(Long id) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new ResourceNotFoundException("Course", id);
        }

        // 查询章节列表
        List<Chapter> chapters = chapterMapper.selectList(
                new LambdaQueryWrapper<Chapter>()
                        .eq(Chapter::getCourseId, id)
                        .orderByAsc(Chapter::getOrderIndex)
        );

        List<ChapterResponse> chapterResponses = chapters.stream()
                .map(this::convertChapterToResponse)
                .collect(Collectors.toList());

        // 获取分类信息
        CategoryInfo categoryInfo = null;
        if (course.getCategoryId() != null) {
            Category category = categoryMapper.selectById(course.getCategoryId());
            if (category != null) {
                categoryInfo = CategoryInfo.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .sortOrder(category.getSortOrder())
                        .build();
            }
        }

        return CourseDetailResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .coverUrl(course.getCoverUrl())
                .duration(course.getDuration())
                .difficulty(course.getDifficulty())
                .pointsReward(course.getPointsReward())
                .status(course.getStatus())
                .categoryId(course.getCategoryId())
                .category(categoryInfo)
                .createdBy(course.getCreatedBy())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .chapters(chapterResponses)
                .build();
    }

    /**
     * 创建课程
     */
    @Transactional
    public CourseResponse createCourse(CourseRequest request, Long createdBy) {
        Course course = new Course();
        BeanUtils.copyProperties(request, course);
        course.setCreatedBy(createdBy);

        // 设置默认值
        if (course.getDifficulty() == null) {
            course.setDifficulty("BEGINNER");
        }
        if (course.getPointsReward() == null) {
            course.setPointsReward(100);
        }
        if (course.getStatus() == null) {
            course.setStatus("DRAFT");
        }

        courseMapper.insert(course);

        return convertToResponse(course);
    }

    /**
     * 更新课程
     */
    @Transactional
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new ResourceNotFoundException("Course", id);
        }

        BeanUtils.copyProperties(request, course);
        courseMapper.updateById(course);

        return convertToResponse(course);
    }

    /**
     * 删除课程
     */
    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new ResourceNotFoundException("Course", id);
        }

        // 删除关联的章节
        chapterMapper.delete(new LambdaQueryWrapper<Chapter>().eq(Chapter::getCourseId, id));

        // 删除课程
        courseMapper.deleteById(id);
    }

    /**
     * 转换为响应 DTO
     */
    private CourseResponse convertToResponse(Course course) {
        // 获取分类信息
        CategoryInfo categoryInfo = null;
        if (course.getCategoryId() != null) {
            Category category = categoryMapper.selectById(course.getCategoryId());
            if (category != null) {
                categoryInfo = CategoryInfo.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .sortOrder(category.getSortOrder())
                        .build();
            }
        }

        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .coverUrl(course.getCoverUrl())
                .duration(course.getDuration())
                .difficulty(course.getDifficulty())
                .pointsReward(course.getPointsReward())
                .status(course.getStatus())
                .categoryId(course.getCategoryId())
                .category(categoryInfo)
                .createdBy(course.getCreatedBy())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }

    /**
     * 转换章节为响应 DTO
     */
    private ChapterResponse convertChapterToResponse(Chapter chapter) {
        return ChapterResponse.builder()
                .id(chapter.getId())
                .courseId(chapter.getCourseId())
                .title(chapter.getTitle())
                .description(chapter.getDescription())
                .videoUrl(chapter.getVideoUrl())
                .duration(chapter.getDuration())
                .orderIndex(chapter.getOrderIndex())
                .createdAt(chapter.getCreatedAt())
                .build();
    }
}
