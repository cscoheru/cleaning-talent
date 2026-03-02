import request from '@/utils/request'
import type {
  Course,
  CourseDetail,
  PageResponse,
  CourseQuery
} from '@/types'

/**
 * 获取课程列表
 */
export const getCourses = (params: CourseQuery) => {
  return request.get<PageResponse<Course>>('/courses', { params })
}

/**
 * 获取课程详情
 */
export const getCourseDetail = (id: number) => {
  return request.get<CourseDetail>(`/courses/${id}`)
}

/**
 * 创建课程（需要 ADMIN 权限）
 */
export const createCourse = (data: Partial<Course>) => {
  return request.post<Course>('/courses', data)
}

/**
 * 更新课程（需要 ADMIN 权限）
 */
export const updateCourse = (id: number, data: Partial<Course>) => {
  return request.put<Course>(`/courses/${id}`, data)
}

/**
 * 删除课程（需要 ADMIN 权限）
 */
export const deleteCourse = (id: number) => {
  return request.delete<{ message: string }>(`/courses/${id}`)
}
