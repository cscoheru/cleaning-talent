import request from '@/utils/request'
import type {
  LoginRequest,
  LoginResponse,
  User
} from '@/types'

/**
 * 用户登录
 */
export const login = (data: LoginRequest) => {
  return request.post<LoginResponse>('/auth/login', data)
}

/**
 * 用户登出
 */
export const logout = () => {
  return request.post<{ message: string }>('/auth/logout')
}

/**
 * 刷新 Token
 */
export const refreshToken = (refreshToken: string) => {
  return request.post<LoginResponse>('/auth/refresh', { refreshToken })
}

/**
 * 获取当前用户信息
 */
export const getCurrentUser = () => {
  return request.get<User>('/auth/me')
}
