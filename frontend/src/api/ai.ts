import request from '@/utils/request'
import type {
  AIChatRequest,
  AIChatResponse,
  RecommendationsResponse
} from '@/types'

/**
 * AI 问答
 */
export const aiChat = (data: AIChatRequest) => {
  // AI 服务可能有不同的 baseURL
  return request.post<AIChatResponse>('/ai/chat', data)
}

/**
 * 获取学习推荐
 */
export const getRecommendations = (userId: number) => {
  return request.get<RecommendationsResponse>('/ai/recommendations', {
    params: { userId }
  })
}
