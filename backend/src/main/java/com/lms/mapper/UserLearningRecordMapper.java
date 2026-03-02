package com.lms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lms.entity.UserLearningRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户学习记录 Mapper
 */
@Mapper
public interface UserLearningRecordMapper extends BaseMapper<UserLearningRecord> {
}
