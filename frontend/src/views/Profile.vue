<template>
  <div class="profile">
    <div class="profile-container">
      <!-- 用户信息卡片 -->
      <el-card class="user-card">
        <div class="user-header">
          <el-avatar :size="80" :src="userStore.userInfo?.avatarUrl">
            <el-icon :size="40"><User /></el-icon>
          </el-avatar>
          <div class="user-info">
            <h2 class="user-name">{{ userStore.userName }}</h2>
            <p class="user-email">{{ userStore.userInfo?.email }}</p>
            <el-tag :type="userStore.isAdmin ? 'danger' : 'primary'">
              {{ userStore.userRole === 'ADMIN' ? '管理员' : '普通用户' }}
            </el-tag>
          </div>
        </div>
        <div class="user-stats">
          <div class="stat-item">
            <p class="stat-value">{{ userStore.userPoints }}</p>
            <p class="stat-label">积分</p>
          </div>
          <div class="stat-item">
            <p class="stat-value">{{ completedCourses }}</p>
            <p class="stat-label">已完成课程</p>
          </div>
          <div class="stat-item">
            <p class="stat-value">{{ learningHours }}h</p>
            <p class="stat-label">学习时长</p>
          </div>
        </div>
      </el-card>

      <!-- 积分历史 -->
      <el-card class="points-card">
        <template #header>
          <div class="card-header">
            <span>积分记录</span>
            <el-button text @click="router.push({ name: 'Ranking' })">
              查看排行榜
              <el-icon><ArrowRight /></el-icon>
            </el-button>
          </div>
        </template>
        <el-table :data="pointsHistory" stripe>
          <el-table-column prop="description" label="说明" />
          <el-table-column label="类型" width="100">
            <template #default="{ row }">
              <el-tag :type="getPointsType(row.type)">
                {{ getPointsTypeText(row.type) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="积分" width="100" align="right">
            <template #default="{ row }">
              <span :class="row.points > 0 ? 'positive' : 'negative'">
                {{ row.points > 0 ? '+' : '' }}{{ row.points }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="createdAt" label="时间" width="180">
            <template #default="{ row }">
              {{ formatDate(row.createdAt) }}
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { User, ArrowRight } from '@element-plus/icons-vue'
import type { PointsRecord } from '@/types'

const router = useRouter()
const userStore = useUserStore()

const completedCourses = ref(8)
const learningHours = ref(42)
const pointsHistory = ref<PointsRecord[]>([])

const getPointsType = (type: string) => {
  const map: Record<string, any> = {
    COURSE_COMPLETE: 'success',
    DAILY_LOGIN: 'info',
    AI_CHAT: 'warning',
    EXAM_PASS: 'success',
    ADMIN_ADJUST: 'danger'
  }
  return map[type] || 'info'
}

const getPointsTypeText = (type: string) => {
  const map: Record<string, string> = {
    COURSE_COMPLETE: '课程完成',
    DAILY_LOGIN: '每日登录',
    AI_CHAT: 'AI 对话',
    EXAM_PASS: '考试通过',
    ADMIN_ADJUST: '管理员调整'
  }
  return map[type] || type
}

const formatDate = (date: string) => {
  return new Date(date).toLocaleString('zh-CN')
}

onMounted(async () => {
  // TODO: 加载用户数据
  pointsHistory.value = [
    {
      id: 1,
      points: 100,
      type: 'COURSE_COMPLETE',
      description: '完成《Vue 3 快速入门》课程',
      createdAt: '2026-03-01T10:00:00Z'
    },
    {
      id: 2,
      points: 50,
      type: 'DAILY_LOGIN',
      description: '每日登录奖励',
      createdAt: '2026-03-01T08:00:00Z'
    }
  ]
})
</script>

<style scoped>
.profile {
  max-width: 1000px;
  margin: 0 auto;
}

.profile-container {
  display: grid;
  grid-template-columns: 1fr;
  gap: 24px;
}

.user-card {
  border-radius: 16px;
}

.user-header {
  display: flex;
  gap: 24px;
  align-items: center;
  padding-bottom: 24px;
  border-bottom: 1px solid #e2e8f0;
  margin-bottom: 24px;
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 24px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 8px 0;
}

.user-email {
  font-size: 14px;
  color: #64748b;
  margin: 0 0 12px 0;
}

.user-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #0d9488;
  margin: 0 0 4px 0;
}

.stat-label {
  font-size: 13px;
  color: #64748b;
  margin: 0;
}

.points-card {
  border-radius: 16px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.positive {
  color: #10b981;
  font-weight: 600;
}

.negative {
  color: #ef4444;
  font-weight: 600;
}

/* 响应式 */
@media (max-width: 768px) {
  .user-header {
    flex-direction: column;
    text-align: center;
  }

  .user-stats {
    grid-template-columns: 1fr;
  }
}
</style>
