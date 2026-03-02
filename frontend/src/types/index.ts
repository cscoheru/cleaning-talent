// ============== 通用类型 ==============

export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
  timestamp: number
}

export interface ApiError {
  code: number
  message: string
  data: null
  timestamp: number
  errors?: ValidationError[]
}

export interface ValidationError {
  field: string
  message: string
}

export interface PageResponse<T> {
  items: T[]
  total: number
  page: number
  pageSize: number
  totalPages: number
}

// ============== 用户类型 ==============

export type UserRole = 'USER' | 'ADMIN'

export interface User {
  id: number
  email: string
  name: string
  avatarUrl?: string
  points: number
  role: UserRole
}

export interface LoginRequest {
  email: string
  password: string
}

export interface LoginResponse {
  token: string
  refreshToken: string
  user: User
}

// ============== 课程类型 ==============

export type Difficulty = 'BEGINNER' | 'INTERMEDIATE' | 'ADVANCED'
export type CourseStatus = 'DRAFT' | 'PUBLISHED' | 'ARCHIVED'

export interface Category {
  id: number
  name: string
}

export interface Chapter {
  id: number
  title: string
  description: string
  videoUrl?: string
  duration: number // 秒
  orderIndex: number
}

export interface Course {
  id: number
  title: string
  description: string
  coverUrl?: string
  duration: number // 总时长（分钟）
  difficulty: Difficulty
  pointsReward: number
  category: Category
  status: CourseStatus
  createdAt: string
}

export interface CourseDetail extends Course {
  chapters: Chapter[]
}

export interface CourseQuery {
  page?: number
  size?: number
  category?: number
  difficulty?: Difficulty
  status?: CourseStatus
}

// ============== 学习类型 ==============

export type LearningStatus = 'NOT_STARTED' | 'IN_PROGRESS' | 'COMPLETED'

export interface LearningProgress {
  courseId: number
  completedLessons: number
  totalLessons: number
  progressPercentage: number
  status: LearningStatus
  enrollmentDate: string
  lastAccessAt: string
}

export interface LearningRecord {
  id: number
  userId: number
  lessonId: number
  courseId: number
  duration: number
  createdAt: string
}

export interface LearningHistory {
  courseId: number
  courseTitle: string
  courseCoverUrl?: string
  progressPercentage: number
  status: LearningStatus
  completedAt?: string
  enrollmentDate: string
}

export interface RecordLearningRequest {
  lessonId: number
  duration: number
}

export interface CompleteCourseRequest {
  courseId: number
}

export interface CompleteCourseResponse {
  message: string
  pointsEarned: number
}

// ============== 积分类型 ==============

export type PointsType =
  | 'COURSE_COMPLETE'
  | 'DAILY_LOGIN'
  | 'AI_CHAT'
  | 'EXAM_PASS'
  | 'ADMIN_ADJUST'

export interface PointsBalance {
  balance: number
  rank: number
}

export interface PointsRecord {
  id: number
  points: number
  type: PointsType
  description: string
  createdAt: string
}

export interface PointsHistoryQuery {
  page?: number
  size?: number
  type?: PointsType
}

export interface RankingItem {
  rank: number
  userId: number
  userName: string
  avatarUrl?: string
  points: number
}

// ============== AI 类型 ==============

export interface AIChatRequest {
  question: string
  context?: {
    courseId?: number
    lessonId?: number
  }
}

export interface AIChatResponse {
  answer: string
  sources?: Source[]
}

export interface Source {
  title: string
  url: string
}

export interface Recommendation {
  id: number
  title: string
  coverUrl?: string
  reason: string
  matchScore: number
}

export interface RecommendationsResponse {
  courses: Recommendation[]
}

// ============== 路由元信息 ==============

export interface RouteMetaCustom {
  title?: string
  requiresAuth?: boolean
  hideInMenu?: boolean
  icon?: string
}
