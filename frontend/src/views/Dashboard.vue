<template>
  <div class="dashboard">
    <!-- 欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-content">
        <h1 class="welcome-title">
          欢迎回来，{{ userStore.userName }} 👋
        </h1>
        <p class="welcome-subtitle">
          今天是你学习的第 {{ learningDays }} 天，继续保持！
        </p>
      </div>
      <div class="welcome-stats">
        <div class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #fbbf24 0%, #f59e0b 100%);">
            <el-icon :size="24"><Coin /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-label">当前积分</p>
            <p class="stat-value">{{ userStore.userPoints }}</p>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);">
            <el-icon :size="24"><Reading /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-label">已学课程</p>
            <p class="stat-value">{{ stats.completedCourses }}</p>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #10b981 0%, #059669 100%);">
            <el-icon :size="24"><Clock /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-label">学习时长</p>
            <p class="stat-value">{{ stats.totalHours }}h</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 继续学习 -->
    <div v-if="inProgressCourse" class="section continue-learning">
      <div class="section-header">
        <h2 class="section-title">继续学习</h2>
        <el-button type="primary" @click="continueLearning">
          继续学习
          <el-icon class="ml-1"><ArrowRight /></el-icon>
        </el-button>
      </div>
      <el-card class="course-progress-card" shadow="hover">
        <div class="course-progress">
          <el-image
            :src="inProgressCourse.coverUrl"
            fit="cover"
            class="course-cover"
          >
            <template #error>
              <div class="image-error">
                <el-icon :size="40"><PictureFilled /></el-icon>
              </div>
            </template>
          </el-image>
          <div class="progress-info">
            <h3 class="course-title">{{ inProgressCourse.title }}</h3>
            <p class="course-category">{{ inProgressCourse.category.name }}</p>
            <div class="progress-bar">
              <el-progress
                :percentage="inProgressCourse.progress"
                :stroke-width="8"
                :show-text="false"
                color="#0d9488"
              />
            </div>
            <div class="progress-detail">
              <span>已学 {{ inProgressCourse.completedLessons }}/{{ inProgressCourse.totalLessons }} 节</span>
              <span>{{ inProgressCourse.progress }}%</span>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 推荐课程 -->
    <div class="section recommended">
      <div class="section-header">
        <h2 class="section-title">为你推荐</h2>
        <el-button text @click="router.push({ name: 'CourseList' })">
          查看全部
          <el-icon><ArrowRight /></el-icon>
        </el-button>
      </div>
      <div class="course-grid">
        <CourseCard
          v-for="course in recommendedCourses"
          :key="course.id"
          :course="course"
        />
      </div>
    </div>

    <!-- 学习历史 -->
    <div class="section recent-activity">
      <div class="section-header">
        <h2 class="section-title">最近学习</h2>
        <el-button text @click="router.push({ name: 'LearningHistory' })">
          查看全部
          <el-icon><ArrowRight /></el-icon>
        </el-button>
      </div>
      <el-table :data="recentLearning" stripe style="width: 100%">
        <el-table-column prop="courseTitle" label="课程名称" />
        <el-table-column label="进度" width="200">
          <template #default="{ row }">
            <el-progress
              :percentage="row.progressPercentage"
              :stroke-width="6"
              :show-text="true"
              :color="getProgressColor(row.progressPercentage)"
            />
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastAccessAt" label="最后学习" width="180">
          <template #default="{ row }">
            {{ formatDate(row.lastAccessAt) }}
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { Coin, Reading, Clock, ArrowRight, PictureFilled } from '@element-plus/icons-vue'
import CourseCard from '@/components/CourseCard.vue'
import type { Course, LearningHistory } from '@/types'

const router = useRouter()
const userStore = useUserStore()

const learningDays = ref(15)
const stats = ref({
  completedCourses: 8,
  totalHours: 42
})

const inProgressCourse = ref<{
  id: number
  title: string
  category: { name: string }
  coverUrl?: string
  progress: number
  completedLessons: number
  totalLessons: number
} | null>(null)

const recommendedCourses = ref<Course[]>([])
const recentLearning = ref<LearningHistory[]>([])

const continueLearning = () => {
  if (inProgressCourse.value) {
    router.push({
      name: 'Learning',
      params: { courseId: inProgressCourse.value.id }
    })
  }
}

const getProgressColor = (percentage: number) => {
  if (percentage >= 80) return '#10b981'
  if (percentage >= 50) return '#3b82f6'
  if (percentage >= 20) return '#f59e0b'
  return '#ef4444'
}

const getStatusType = (status: string) => {
  const typeMap: Record<string, any> = {
    COMPLETED: 'success',
    IN_PROGRESS: 'warning',
    NOT_STARTED: 'info'
  }
  return typeMap[status] || 'info'
}

const getStatusText = (status: string) => {
  const textMap: Record<string, string> = {
    COMPLETED: '已完成',
    IN_PROGRESS: '学习中',
    NOT_STARTED: '未开始'
  }
  return textMap[status] || status
}

const formatDate = (date: string) => {
  const d = new Date(date)
  const now = new Date()
  const diff = now.getTime() - d.getTime()
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))

  if (days === 0) return '今天'
  if (days === 1) return '昨天'
  if (days < 7) return `${days} 天前`
  return d.toLocaleDateString('zh-CN')
}

const loadDashboardData = async () => {
  // TODO: 调用 API 获取真实数据
  // Mock data for demo
  inProgressCourse.value = {
    id: 1,
    title: 'Vue 3 快速入门',
    category: { name: '前端开发' },
    coverUrl: 'https://images.unsplash.com/photo-1633356122544-f134324a6cee?w=400',
    progress: 65,
    completedLessons: 13,
    totalLessons: 20
  }

  recommendedCourses.value = [
    {
      id: 2,
      title: 'Spring Boot 实战',
      description: '从零开始构建企业级 Java 应用',
      coverUrl: 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=400',
      duration: 180,
      difficulty: 'INTERMEDIATE',
      pointsReward: 150,
      category: { id: 2, name: '后端开发' },
      status: 'PUBLISHED',
      createdAt: '2026-03-01T00:00:00Z'
    },
    {
      id: 3,
      title: 'TypeScript 进阶',
      description: '深入理解 TypeScript 类型系统',
      coverUrl: 'https://images.unsplash.com/photo-1516116216624-53e697fedbea?w=400',
      duration: 120,
      difficulty: 'ADVANCED',
      pointsReward: 200,
      category: { id: 1, name: '前端开发' },
      status: 'PUBLISHED',
      createdAt: '2026-03-01T00:00:00Z'
    },
    {
      id: 4,
      title: 'Docker 容器化实战',
      description: '掌握容器化部署技术',
      coverUrl: 'https://images.unsplash.com/photo-1605745341112-85968b19335b?w=400',
      duration: 90,
      difficulty: 'BEGINNER',
      pointsReward: 100,
      category: { id: 3, name: '运维' },
      status: 'PUBLISHED',
      createdAt: '2026-03-01T00:00:00Z'
    }
  ]

  recentLearning.value = [
    {
      courseId: 5,
      courseTitle: 'React 18 新特性解析',
      progressPercentage: 100,
      status: 'COMPLETED',
      completedAt: '2026-03-01T10:00:00Z',
      enrollmentDate: '2026-02-25T00:00:00Z',
      lastAccessAt: '2026-03-01T10:00:00Z'
    },
    {
      courseId: 6,
      courseTitle: 'Python 数据分析基础',
      progressPercentage: 45,
      status: 'IN_PROGRESS',
      enrollmentDate: '2026-02-28T00:00:00Z',
      lastAccessAt: '2026-02-28T15:30:00Z'
    },
    {
      courseId: 7,
      courseTitle: 'Git 版本控制精讲',
      progressPercentage: 100,
      status: 'COMPLETED',
      completedAt: '2026-02-20T10:00:00Z',
      enrollmentDate: '2026-02-15T00:00:00Z',
      lastAccessAt: '2026-02-20T10:00:00Z'
    }
  ]
}

onMounted(() => {
  loadDashboardData()
})
</script>

<style scoped>
.dashboard {
  max-width: 1400px;
  margin: 0 auto;
}

/* 欢迎区域 */
.welcome-section {
  margin-bottom: 32px;
}

.welcome-content {
  margin-bottom: 24px;
}

.welcome-title {
  font-size: 32px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 8px 0;
}

.welcome-subtitle {
  font-size: 16px;
  color: #64748b;
  margin: 0;
}

.welcome-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 13px;
  color: #64748b;
  margin: 0 0 4px 0;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #1e293b;
  margin: 0;
}

/* 区块通用样式 */
.section {
  margin-bottom: 32px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  color: #1e293b;
  margin: 0;
}

/* 继续学习 */
.course-progress-card {
  border-radius: 16px;
  overflow: hidden;
}

.course-progress-card :deep(.el-card__body) {
  padding: 0;
}

.course-progress {
  display: flex;
  gap: 24px;
  padding: 20px;
}

.course-cover {
  width: 200px;
  height: 120px;
  border-radius: 12px;
  flex-shrink: 0;
}

.image-error {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f1f5f9;
  color: #94a3b8;
}

.progress-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.course-title {
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 4px 0;
}

.course-category {
  font-size: 13px;
  color: #64748b;
  margin: 0 0 16px 0;
}

.progress-bar {
  margin-bottom: 8px;
}

.progress-detail {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  color: #64748b;
}

/* 推荐课程 */
.course-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

/* 最近学习 */
.recent-activity :deep(.el-table) {
  border-radius: 16px;
  overflow: hidden;
}

.recent-activity :deep(.el-table__header-wrapper) {
  border-radius: 16px 16px 0 0;
}

/* 响应式 */
@media (max-width: 768px) {
  .welcome-stats {
    grid-template-columns: 1fr;
  }

  .course-progress {
    flex-direction: column;
  }

  .course-cover {
    width: 100%;
    height: 160px;
  }

  .course-grid {
    grid-template-columns: 1fr;
  }
}
</style>
