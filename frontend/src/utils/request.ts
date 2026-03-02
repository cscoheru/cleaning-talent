import axios, { AxiosError, InternalAxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'

// 请求配置接口
interface RequestConfig extends InternalAxiosRequestConfig {
  skipAuth?: boolean
  skipErrorHandler?: boolean
}

// 创建 Axios 实例
const request = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api/v1',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Token 管理
const TokenKey = 'access_token'
const RefreshTokenKey = 'refresh_token'

export const getToken = (): string | null => {
  return localStorage.getItem(TokenKey)
}

export const setToken = (token: string): void => {
  localStorage.setItem(TokenKey, token)
}

export const getRefreshToken = (): string | null => {
  return localStorage.getItem(RefreshTokenKey)
}

export const setRefreshToken = (token: string): void => {
  localStorage.setItem(RefreshTokenKey, token)
}

export const clearTokens = (): void => {
  localStorage.removeItem(TokenKey)
  localStorage.removeItem(RefreshTokenKey)
}

// 是否正在刷新 Token
let isRefreshing = false
// 失败队列
let failedQueue: Array<{
  resolve: (value?: any) => void
  reject: (reason?: any) => void
}> = []

const processQueue = (error: any, token: string | null = null) => {
  failedQueue.forEach(prom => {
    if (error) {
      prom.reject(error)
    } else {
      prom.resolve(token)
    }
  })
  failedQueue = []
}

// 请求拦截器
request.interceptors.request.use(
  (config: RequestConfig) => {
    // 如果不需要跳过认证，添加 Token
    if (!config.skipAuth) {
      const token = getToken()
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
      }
    }
    return config
  },
  (error: AxiosError) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response: AxiosResponse) => {
    const { code, message, data } = response.data

    // 成功响应
    if (code === 200) {
      return data
    }

    // 业务错误
    if (!response.config.skipErrorHandler) {
      ElMessage.error(message || '请求失败')
    }
    return Promise.reject(new Error(message || '请求失败'))
  },
  async (error: AxiosError) => {
    const originalRequest = error.config as RequestConfig

    // 网络错误或超时
    if (!error.response) {
      if (!originalRequest.skipErrorHandler) {
        ElMessage.error('网络连接失败，请检查网络设置')
      }
      return Promise.reject(error)
    }

    const { status, data } = error.response

    // 401 未认证 - 尝试刷新 Token
    if (status === 401 && !originalRequest.skipAuth && !originalRequest._retry) {
      if (isRefreshing) {
        // 如果正在刷新，将请求加入队列
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject })
        }).then(token => {
          if (originalRequest.headers) {
            originalRequest.headers.Authorization = `Bearer ${token}`
          }
          return request(originalRequest)
        }).catch(err => {
          return Promise.reject(err)
        })
      }

      originalRequest._retry = true
      isRefreshing = true

      const refreshToken = getRefreshToken()
      if (!refreshToken) {
        // 没有刷新 Token，清除本地存储并跳转登录
        clearTokens()
        window.location.href = '/login'
        return Promise.reject(error)
      }

      try {
        // 调用刷新 Token 接口
        const response = await axios.post('/api/v1/auth/refresh', {
          refreshToken
        })

        const { token, refreshToken: newRefreshToken } = response.data.data
        setToken(token)
        setRefreshToken(newRefreshToken)

        // 处理队列中的请求
        processQueue(null, token)

        // 重试原请求
        if (originalRequest.headers) {
          originalRequest.headers.Authorization = `Bearer ${token}`
        }
        return request(originalRequest)
      } catch (refreshError) {
        // 刷新失败，清除 Token 并跳转登录
        processQueue(refreshError, null)
        clearTokens()
        window.location.href = '/login'
        return Promise.reject(refreshError)
      } finally {
        isRefreshing = false
      }
    }

    // 403 无权限
    if (status === 403) {
      if (!originalRequest.skipErrorHandler) {
        ElMessage.error('您没有权限执行此操作')
      }
      return Promise.reject(error)
    }

    // 404 资源不存在
    if (status === 404) {
      if (!originalRequest.skipErrorHandler) {
        ElMessage.error('请求的资源不存在')
      }
      return Promise.reject(error)
    }

    // 429 请求过于频繁
    if (status === 429) {
      if (!originalRequest.skipErrorHandler) {
        ElMessage.error('请求过于频繁，请稍后再试')
      }
      return Promise.reject(error)
    }

    // 500 服务器错误
    if (status >= 500) {
      if (!originalRequest.skipErrorHandler) {
        ElMessage.error('服务器错误，请稍后再试')
      }
      return Promise.reject(error)
    }

    // 其他错误
    if (!originalRequest.skipErrorHandler) {
      const message = (data as any)?.message || '请求失败'
      ElMessage.error(message)
    }

    return Promise.reject(error)
  }
)

export default request
