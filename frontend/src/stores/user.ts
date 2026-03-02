import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi, logout as logoutApi, getCurrentUser } from '@/api/auth'
import type { User, LoginRequest } from '@/types'
import {
  setToken,
  setRefreshToken,
  clearTokens,
  getToken
} from '@/utils/request'

export const useUserStore = defineStore('user', () => {
  // State
  const token = ref<string>(getToken() || '')
  const userInfo = ref<User | null>(null)
  const isLoading = ref(false)

  // Getters
  const isLoggedIn = computed(() => !!token.value)
  const userName = computed(() => userInfo.value?.name || '')
  const userPoints = computed(() => userInfo.value?.points || 0)
  const userRole = computed(() => userInfo.value?.role || 'USER')
  const isAdmin = computed(() => userRole.value === 'ADMIN')

  // Actions
  /**
   * 用户登录
   */
  const login = async (credentials: LoginRequest) => {
    isLoading.value = true
    try {
      const res = await loginApi(credentials)
      token.value = res.token
      userInfo.value = res.user
      setToken(res.token)
      setRefreshToken(res.refreshToken)
      return res
    } finally {
      isLoading.value = false
    }
  }

  /**
   * 获取用户信息
   */
  const fetchUserInfo = async () => {
    if (!token.value) return

    isLoading.value = true
    try {
      const user = await getCurrentUser()
      userInfo.value = user
      return user
    } catch (error) {
      // Token 可能已失效，清除本地状态
      logout()
      throw error
    } finally {
      isLoading.value = false
    }
  }

  /**
   * 用户登出
   */
  const logout = async () => {
    try {
      await logoutApi()
    } catch {
      // 忽略登出 API 错误
    } finally {
      token.value = ''
      userInfo.value = null
      clearTokens()
    }
  }

  /**
   * 更新用户积分
   */
  const updatePoints = (points: number) => {
    if (userInfo.value) {
      userInfo.value.points = points
    }
  }

  return {
    // State
    token,
    userInfo,
    isLoading,
    // Getters
    isLoggedIn,
    userName,
    userPoints,
    userRole,
    isAdmin,
    // Actions
    login,
    fetchUserInfo,
    logout,
    updatePoints
  }
})
