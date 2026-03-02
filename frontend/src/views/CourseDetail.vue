<template>
  <div v-loading="loading" class="course-detail">
    <div v-if="course" class="detail-container">
      <!-- 返回按钮 -->
      <el-button
        :icon="ArrowLeft"
        text
        @click="router.back()"
        class="back-btn"
      >
        返回课程列表
      </el-button>

      <!-- 课程头部 -->
      <div class="course-header">
        <div class="header-left">
          <el-image
            :src="course.coverUrl"
            fit="cover"
            class="course-cover"
          >
            <template #error>
              <div class="image-error">
                <el-icon :size="60"><PictureFilled /></el-icon>
              </div>
            </template>
          </el-image>
        </div>
        <div class="header-right">
          <div class="course-meta-top">
            <el-tag :type="getDifficultyType(course.difficulty)" size="large">
              {{ getDifficultyText(course.difficulty) }}
            </el-tag>
            <el-tag type="info" size="large">
              <el-icon><Collection /></el-icon>
              {{ course.category.name }}
            </el-tag>
          </div>
          <h1 class="course-title">{{ course.title }}</h1>
          <p class="course-description">{{ course.description }}</p>
          <div class="course-stats">
            <div class="stat-item">
              <el-icon><Clock /></el-icon>
              <span>{{ course.duration }} 分钟</span>
            </div>
            <div class="stat-item">
              <el-icon><Coin /></el-icon>
              <span>完成获得 {{ course.pointsReward }} 积分</span>
            </div>
            <div class="stat-item">
              <el-icon><Document /></el-icon>
              <span>{{ course.chapters?.length || 0 }} 个章节</span>
            </div>
          </div>
          <div class="course-actions">
            <el-button
              type="primary"
              size="large"
              :icon="VideoPlay"
              @click="startLearning"
            >
              开始学习
            </el-button>
            <el-button size="large" :icon="Star">收藏</el-button>
          </div>
        </div>
      </div>

      <!-- 章节列表 -->
      <div class="chapters-section">
        <h2 class="section-title">课程目录</h2>
        <el-collapse v-model="activeChapters" class="chapters-collapse">
          <el-collapse-item
            v-for="chapter in course.chapters"
            :key="chapter.id"
            :name="chapter.id"
          >
            <template #title>
              <div class="chapter-title">
                <span class="chapter-order">{{ chapter.orderIndex }}</span>
                <span class="chapter-name">{{ chapter.title }}</span>
                <span class="chapter-duration">{{ formatDuration(chapter.duration) }}</span>
              </div>
            </template>
            <div class="chapter-content">
              <p class="chapter-description">{{ chapter.description }}</p>
              <div v-if="chapter.videoUrl" class="chapter-actions">
                <el-button type="primary" text @click.stop="playChapter(chapter)">
                  <el-icon><VideoPlay /></el-icon>
                  播放视频
                </el-button>
              </div>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </div>

    <!-- 未找到课程 -->
    <div v-else-if="!loading" class="not-found">
      <el-empty description="课程不存在或已被删除">
        <el-button type="primary" @click="router.push({ name: 'CourseList' })">
          返回课程列表
        </el-button>
      </el-empty>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  ArrowLeft,
  PictureFilled,
  Collection,
  Clock,
  Coin,
  Document,
  VideoPlay,
  Star
} from '@element-plus/icons-vue'
import type { CourseDetail } from '@/types'

const router = useRouter()
const route = useRoute()

const loading = ref(true)
const course = ref<CourseDetail | null>(null)
const activeChapters = ref<number[]>([])

const getDifficultyType = (difficulty: string) => {
  const map: Record<string, any> = {
    BEGINNER: 'success',
    INTERMEDIATE: 'warning',
    ADVANCED: 'danger'
  }
  return map[difficulty] || 'info'
}

const getDifficultyText = (difficulty: string) => {
  const map: Record<string, string> = {
    BEGINNER: '入门',
    INTERMEDIATE: '进阶',
    ADVANCED: '高级'
  }
  return map[difficulty] || difficulty
}

const formatDuration = (seconds: number) => {
  const minutes = Math.floor(seconds / 60)
  if (minutes < 60) return `${minutes} 分钟`
  const hours = Math.floor(minutes / 60)
  const remainingMinutes = minutes % 60
  return remainingMinutes > 0 ? `${hours} 小时 ${remainingMinutes} 分钟` : `${hours} 小时`
}

const startLearning = () => {
  if (course.value) {
    router.push({
      name: 'Learning',
      params: { courseId: course.value.id }
    })
  }
}

const playChapter = (chapter: any) => {
  router.push({
    name: 'Learning',
    params: { courseId: route.params.id },
    query: { chapter: chapter.id }
  })
}

const loadCourseDetail = async () => {
  loading.value = true
  try {
    const courseId = Number(route.params.id)
    // TODO: 调用真实 API
    // const data = await getCourseDetail(courseId)
    // course.value = data

    // Mock data
    await new Promise(resolve => setTimeout(resolve, 500))
    course.value = {
      id: courseId,
      title: 'Vue 3 快速入门',
      description: '本课程将带你从零开始学习 Vue 3，掌握组合式 API、响应式原理、组件通信等核心概念，并通过实战项目巩固所学知识。',
      coverUrl: 'https://images.unsplash.com/photo-1633356122544-f134324a6cee?w=800',
      duration: 120,
      difficulty: 'BEGINNER',
      pointsReward: 100,
      category: { id: 1, name: '前端开发' },
      status: 'PUBLISHED',
      createdAt: '2026-03-01T00:00:00Z',
      chapters: [
        {
          id: 1,
          title: 'Vue 3 简介',
          description: '了解 Vue 3 的新特性和开发环境搭建',
          videoUrl: 'https://example.com/video1.mp4',
          duration: 600,
          orderIndex: 1
        },
        {
          id: 2,
          title: '组合式 API 基础',
          description: '学习 ref、reactive、computed 等核心 API',
          videoUrl: 'https://example.com/video2.mp4',
          duration: 900,
          orderIndex: 2
        },
        {
          id: 3,
          title: '组件通信',
          description: '掌握 props、emit、provide/inject 等通信方式',
          videoUrl: 'https://example.com/video3.mp4',
          duration: 1200,
          orderIndex: 3
        },
        {
          id: 4,
          title: 'Vue Router 路由',
          description: '学习 Vue Router 4 的使用方法和技巧',
          videoUrl: 'https://example.com/video4.mp4',
          duration: 900,
          orderIndex: 4
        },
        {
          id: 5,
          title: 'Pinia 状态管理',
          description: '使用 Pinia 进行全局状态管理',
          videoUrl: 'https://example.com/video5.mp4',
          duration: 720,
          orderIndex: 5
        }
      ]
    }

    // 默认展开所有章节
    if (course.value.chapters) {
      activeChapters.value = course.value.chapters.map(c => c.id)
    }
  } catch (error) {
    console.error('Failed to load course detail:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadCourseDetail()
})
</script>

<style scoped>
.course-detail {
  max-width: 1200px;
  margin: 0 auto;
}

.back-btn {
  margin-bottom: 24px;
  font-size: 14px;
  color: #64748b;
}

/* 课程头部 */
.course-header {
  display: grid;
  grid-template-columns: 400px 1fr;
  gap: 32px;
  margin-bottom: 40px;
}

.course-cover {
  width: 100%;
  height: 250px;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
}

.image-error {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
  color: #94a3b8;
}

.header-right {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.course-meta-top {
  display: flex;
  gap: 12px;
}

.course-title {
  font-size: 32px;
  font-weight: 700;
  color: #1e293b;
  margin: 0;
}

.course-description {
  font-size: 16px;
  color: #64748b;
  line-height: 1.6;
  margin: 0;
}

.course-stats {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  padding: 20px 0;
  border-top: 1px solid #e2e8f0;
  border-bottom: 1px solid #e2e8f0;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #475569;
}

.stat-item .el-icon {
  color: #0d9488;
}

.course-actions {
  display: flex;
  gap: 12px;
}

.course-actions .el-button {
  border-radius: 12px;
  padding: 0 32px;
}

/* 章节列表 */
.chapters-section {
  background: #fff;
  border-radius: 20px;
  padding: 32px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.section-title {
  font-size: 24px;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 24px 0;
}

.chapters-collapse {
  border: none;
}

.chapters-collapse :deep(.el-collapse-item) {
  margin-bottom: 12px;
  border-radius: 12px;
  overflow: hidden;
}

.chapters-collapse :deep(.el-collapse-item__header) {
  background: #f8fafc;
  border: none;
  padding: 0 20px;
  height: 60px;
  border-radius: 12px;
}

.chapters-collapse :deep(.el-collapse-item__wrap) {
  border: none;
  background: transparent;
}

.chapters-collapse :deep(.el-collapse-item__content) {
  padding: 0 20px 20px;
}

.chapter-title {
  display: flex;
  align-items: center;
  gap: 16px;
  width: 100%;
  font-size: 15px;
}

.chapter-order {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #0d9488;
  color: #fff;
  border-radius: 50%;
  font-size: 13px;
  font-weight: 600;
  flex-shrink: 0;
}

.chapter-name {
  flex: 1;
  font-weight: 500;
  color: #334155;
}

.chapter-duration {
  font-size: 13px;
  color: #94a3b8;
}

.chapter-content {
  padding: 16px 0;
}

.chapter-description {
  font-size: 14px;
  color: #64748b;
  margin: 0 0 16px 0;
  line-height: 1.6;
}

/* 未找到 */
.not-found {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

/* 响应式 */
@media (max-width: 768px) {
  .course-header {
    grid-template-columns: 1fr;
  }

  .course-cover {
    height: 200px;
  }

  .course-title {
    font-size: 24px;
  }

  .chapters-section {
    padding: 20px;
  }
}
</style>
