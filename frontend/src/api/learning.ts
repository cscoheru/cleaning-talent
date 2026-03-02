import request from '@/utils/request'
import type {
  LearningProgress,
  PageResponse,
  LearningRecord,
  LearningHistory,
  RecordLearningRequest,
  CompleteCourseRequest,
  CompleteCourseResponse
} from '@/types'

/**
 * 获取学习进度
 */
export const getLearningProgress = (courseId?: number) => {
  return request.get<LearningProgress>('/learning/progress', {
    params: { courseId }
  })
}

/**
 * 记录学习
 */
export const recordLearning = (data: RecordLearningRequest) => {
  return request.post<LearningRecord>('/learning/record', data)
}

/**
 * 完成课程
 */
export const completeCourse = (data: CompleteCourseRequest) => {
  return request.post<CompleteCourseResponse>('/learning/complete', data)
}

/**
 * 获取学习历史
 */
export const getLearningHistory = (params: { page?: number; size?: number }) => {
  return request.get<PageResponse<LearningHistory>>('/learning/history', { params })
}
