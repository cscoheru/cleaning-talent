import request from '@/utils/request'
import type {
  PointsBalance,
  PageResponse,
  PointsRecord,
  PointsHistoryQuery,
  RankingItem
} from '@/types'

/**
 * 获取积分余额
 */
export const getPointsBalance = () => {
  return request.get<PointsBalance>('/points/balance')
}

/**
 * 获取积分历史
 */
export const getPointsHistory = (params: PointsHistoryQuery) => {
  return request.get<PageResponse<PointsRecord>>('/points/history', { params })
}

/**
 * 获取积分排行榜
 */
export const getPointsRanking = (params: { page?: number; size?: number }) => {
  return request.get<PageResponse<RankingItem>>('/points/ranking', { params })
}
