<template>
  <div v-loading="loading" class="learning-page">
    <div v-if="course" class="learning-container">
      <!-- 返回按钮 -->
      <div class="learning-header">
        <el-button
          :icon="ArrowLeft"
          text
          @click="router.back()"
        >
          返回课程详情
        </el-button>
      </div>

      <!-- 主要内容 -->
      <div class="learning-content">
        <!-- 视频播放器区域 -->
        <div class="video-section">
          <div class="video-player">
            <div class="video-placeholder">
              <el-icon :size="80" color="#94a3b8"><VideoPlay /></el-icon>
              <p>{{ currentChapter?.title || '选择章节开始学习' }}</p>
            </div>
          </div>

          <!-- 播放控制 -->
          <div class="playback-controls">
            <div class="control-buttons">
              <el-button circle :icon="ArrowLeft" :disabled="!hasPrevious" @click="previousChapter" />
              <el-button circle :icon="VideoPlay" type="primary" />
              <el-button circle :icon="ArrowRight" :disabled="!hasNext" @click="nextChapter" />
            </div>
            <div class="progress-section">
              <span class="progress-text">{{ currentProgress }}</span>
              <el-slider v-model="playbackProgress" :show-tooltip="false" />
            </div>
          </div>
        </div>

        <!-- 章节列表 -->
        <div class="chapters-sidebar">
          <div class="sidebar-header">
            <h3>课程目录</h3>
            <span class="progress-badge">{{ completedCount }}/{{ totalCount }} 已完成</span>
          </div>
          <div class="chapter-list">
            <div
              v-for="chapter in course.chapters"
              :key="chapter.id"
              class="chapter-item"
              :class="{
                active: currentChapterId === chapter.id,
                completed: isChapterCompleted(chapter.id)
              }"
              @click="selectChapter(chapter)"
            >
              <div class="chapter-indicator">
                <el-icon v-if="isChapterCompleted(chapter.id)"><CircleCheck /></el-icon>
                <el-icon v-else-if="currentChapterId === chapter.id"><VideoPlay /></el-icon>
                <span v-else class="chapter-number">{{ chapter.orderIndex }}</span>
              </div>
              <div class="chapter-info">
                <p class="chapter-name">{{ chapter.title }}</p>
                <span class="chapter-duration">{{ formatDuration(chapter.duration) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 完成按钮 -->
      <div class="learning-footer">
        <el-button
          v-if="allCompleted"
          type="success"
          size="large"
          @click="handleCompleteCourse"
        >
          <el-icon><CircleCheck /></el-icon>
          完成课程并领取积分
        </el-button>
        <el-button
          v-else
          type="primary"
          size="large"
          @click="markAsCompleted"
        >
          标记当前章节已完成
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  ArrowLeft,
  VideoPlay,
  ArrowRight,
  CircleCheck
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { CourseDetail, Chapter } from '@/types'

const router = useRouter()
const route = useRoute()

const loading = ref(true)
const course = ref<CourseDetail | null>(null)
const currentChapterId = ref<number | null>(null)
const playbackProgress = ref(0)
const completedChapters = ref<Set<number>>(new Set())

const currentChapter = computed(() => {
  if (!course.value || !currentChapterId.value) return null
  return course.value.chapters.find(c => c.id === currentChapterId.value)
})

const currentChapterIndex = computed(() => {
  if (!course.value || !currentChapterId.value) return -1
  return course.value.chapters.findIndex(c => c.id === currentChapterId.value)
})

const hasPrevious = computed(() => currentChapterIndex.value > 0)
const hasNext = computed(() => {
  if (!course.value) return false
  return currentChapterIndex.value < course.value.chapters.length - 1
})

const completedCount = computed(() => completedChapters.value.size)
const totalCount = computed(() => course.value?.chapters.length || 0)
const allCompleted = computed(() => totalCount.value > 0 && completedCount.value === totalCount.value)

const currentProgress = computed(() => {
  if (!currentChapter.value) return '0:00 / 0:00'
  const current = Math.floor(playbackProgress.value * currentChapter.value.duration)
  return `${formatTime(current)} / ${formatTime(currentChapter.value.duration)}`
})

const isChapterCompleted = (id: number) => completedChapters.value.has(id)

const formatDuration = (seconds: number) => {
  return formatTime(seconds)
}

const formatTime = (seconds: number) => {
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${mins}:${secs.toString().padStart(2, '0')}`
}

const selectChapter = (chapter: Chapter) => {
  currentChapterId.value = chapter.id
  playbackProgress.value = 0
}

const previousChapter = () => {
  if (!course.value || !hasPrevious.value) return
  const newIndex = currentChapterIndex.value - 1
  selectChapter(course.value.chapters[newIndex])
}

const nextChapter = () => {
  if (!course.value || !hasNext.value) return
  const newIndex = currentChapterIndex.value + 1
  selectChapter(course.value.chapters[newIndex])
}

const markAsCompleted = () => {
  if (currentChapterId.value) {
    completedChapters.value.add(currentChapterId.value)
    ElMessage.success('章节已标记为完成')
    // 自动跳转到下一章
    if (hasNext.value) {
      nextChapter()
    }
  }
}

const handleCompleteCourse = async () => {
  try {
    // TODO: 调用完成课程 API
    ElMessage.success('恭喜！课程已完成，获得 100 积分')
    router.push({ name: 'Dashboard' })
  } catch (error) {
    ElMessage.error('操作失败，请重试')
  }
}

const loadCourseData = async () => {
  loading.value = true
  try {
    const courseId = Number(route.params.courseId)
    // TODO: 调用真实 API
    await new Promise(resolve => setTimeout(resolve, 500))

    // Mock data
    course.value = {
      id: courseId,
      title: 'Vue 3 快速入门',
      description: '从零开始学习 Vue 3',
      coverUrl: '',
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
          description: '了解 Vue 3 的新特性',
          duration: 600,
          orderIndex: 1
        },
        {
          id: 2,
          title: '组合式 API 基础',
          description: '学习核心 API',
          duration: 900,
          orderIndex: 2
        },
        {
          id: 3,
          title: '组件通信',
          description: '掌握组件间通信方式',
          duration: 1200,
          orderIndex: 3
        },
        {
          id: 4,
          title: 'Vue Router',
          description: '学习路由管理',
          duration: 900,
          orderIndex: 4
        },
        {
          id: 5,
          title: 'Pinia 状态管理',
          description: '使用 Pinia 管理状态',
          duration: 720,
          orderIndex: 5
        }
      ]
    }

    // 选择第一章或指定的章节
    const queryChapter = Number(route.query.chapter)
    const firstChapter = course.value.chapters[0]
    currentChapterId.value = queryChapter || firstChapter?.id || null
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadCourseData()
})
</script>

<style scoped>
.learning-page {
  height: calc(100vh - 120px);
}

.learning-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.learning-header {
  margin-bottom: 16px;
}

.learning-content {
  flex: 1;
  display: grid;
  grid-template-columns: 1fr 360px;
  gap: 24px;
  min-height: 0;
}

/* 视频区域 */
.video-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.video-player {
  flex: 1;
  background: #000;
  border-radius: 16px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.video-placeholder {
  text-align: center;
  color: #fff;
}

.video-placeholder .el-icon {
  margin-bottom: 16px;
}

.video-placeholder p {
  font-size: 16px;
  margin: 0;
}

.playback-controls {
  padding: 20px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.control-buttons {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-bottom: 16px;
}

.progress-section {
  display: flex;
  align-items: center;
  gap: 16px;
}

.progress-text {
  min-width: 120px;
  text-align: center;
  font-size: 14px;
  color: #475569;
  font-variant-numeric: tabular-nums;
}

.progress-section .el-slider {
  flex: 1;
}

/* 章节侧边栏 */
.chapters-sidebar {
  background: #fff;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  max-height: 100%;
}

.sidebar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e2e8f0;
}

.sidebar-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: #1e293b;
  margin: 0;
}

.progress-badge {
  padding: 4px 12px;
  background: #dcfce7;
  color: #166534;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

.chapter-list {
  flex: 1;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.chapter-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.chapter-item:hover {
  background: #f8fafc;
}

.chapter-item.active {
  background: #f0fdfa;
  border: 1px solid #0d9488;
}

.chapter-item.completed {
  opacity: 0.7;
}

.chapter-indicator {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.chapter-indicator .el-icon {
  font-size: 18px;
}

.chapter-indicator .chapter-number {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #e2e8f0;
  border-radius: 50%;
  font-size: 12px;
  font-weight: 600;
  color: #64748b;
}

.chapter-item.active .chapter-indicator .chapter-number {
  background: #0d9488;
  color: #fff;
}

.chapter-item.completed .chapter-indicator .el-icon {
  color: #10b981;
}

.chapter-info {
  flex: 1;
  min-width: 0;
}

.chapter-name {
  font-size: 14px;
  font-weight: 500;
  color: #334155;
  margin: 0 0 4px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.chapter-duration {
  font-size: 12px;
  color: #94a3b8;
}

/* 页脚 */
.learning-footer {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}

/* 响应式 */
@media (max-width: 1024px) {
  .learning-content {
    grid-template-columns: 1fr;
    grid-template-rows: auto 1fr;
  }

  .chapters-sidebar {
    max-height: 300px;
  }
}
</style>
