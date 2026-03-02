<template>
  <el-card class="course-card" shadow="hover" @click="handleClick">
    <div class="course-cover">
      <el-image :src="course.coverUrl" fit="cover" class="cover-image">
        <template #error>
          <div class="image-error">
            <el-icon :size="40"><PictureFilled /></el-icon>
          </div>
          </template>
      </el-image>
      <div class="difficulty-badge" :class="`difficulty-${course.difficulty.toLowerCase()}`">
        {{ difficultyText }}
      </div>
      <div class="points-badge">
        <el-icon><Coin /></el-icon>
        {{ course.pointsReward }}
      </div>
    </div>
    <div class="course-content">
      <h3 class="course-title">{{ course.title }}</h3>
      <p class="course-description">{{ course.description }}</p>
      <div class="course-meta">
        <span class="category">
          <el-icon><Collection /></el-icon>
          {{ course.category.name }}
        </span>
        <span class="duration">
          <el-icon><Clock /></el-icon>
          {{ course.duration }} 分钟
        </span>
      </div>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { PictureFilled, Coin, Collection, Clock } from '@element-plus/icons-vue'
import type { Course } from '@/types'

const props = defineProps<{
  course: Course
}>()

const router = useRouter()

const difficultyText = computed(() => {
  const map: Record<string, string> = {
    BEGINNER: '入门',
    INTERMEDIATE: '进阶',
    ADVANCED: '高级'
  }
  return map[props.course.difficulty] || props.course.difficulty
})

const handleClick = () => {
  router.push({
    name: 'CourseDetail',
    params: { id: props.course.id }
  })
}
</script>

<style scoped>
.course-card {
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #e2e8f0;
}

.course-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
}

.course-card :deep(.el-card__body) {
  padding: 0;
}

.course-cover {
  position: relative;
  width: 100%;
  height: 160px;
  overflow: hidden;
}

.cover-image {
  width: 100%;
  height: 100%;
}

.cover-image :deep(img) {
  transition: transform 0.5s ease;
}

.course-card:hover .cover-image :deep(img) {
  transform: scale(1.05);
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

.difficulty-badge {
  position: absolute;
  top: 12px;
  left: 12px;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  color: #fff;
}

.difficulty-beginner {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
}

.difficulty-intermediate {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
}

.difficulty-advanced {
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
}

.points-badge {
  position: absolute;
  top: 12px;
  right: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 6px 12px;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  color: #fbbf24;
}

.course-content {
  padding: 16px;
}

.course-title {
  font-size: 16px;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 8px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.course-description {
  font-size: 13px;
  color: #64748b;
  margin: 0 0 12px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  height: 36px;
  line-height: 18px;
}

.course-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #94a3b8;
}

.course-meta span {
  display: flex;
  align-items: center;
  gap: 4px;
}
</style>
