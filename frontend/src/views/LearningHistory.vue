<template>
  <div class="learning-history">
    <div class="page-header">
      <h1 class="page-title">学习历史</h1>
    </div>

    <el-card v-loading="loading" class="history-card">
      <el-table :data="historyList" stripe>
        <el-table-column prop="courseTitle" label="课程名称" min-width="200" />
        <el-table-column label="进度" width="200">
          <template #default="{ row }">
            <el-progress
              :percentage="row.progressPercentage"
              :stroke-width="8"
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
        <el-table-column label="报名时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.enrollmentDate) }}
          </template>
        </el-table-column>
        <el-table-column label="完成时间" width="180">
          <template #default="{ row }">
            {{ row.completedAt ? formatDate(row.completedAt) : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.status !== 'COMPLETED'"
              type="primary"
              text
              @click="continueLearning(row)"
            >
              继续学习
            </el-button>
            <el-button v-else text @click="reviewCourse(row)">
              查看证书
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="total"
          layout="total, sizes, prev, pager, next"
          @size-change="loadHistory"
          @current-change="loadHistory"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import type { LearningHistory } from '@/types'

const router = useRouter()
const loading = ref(false)
const historyList = ref<LearningHistory[]>([])
const total = ref(0)

const pagination = reactive({
  page: 1,
  size: 20
})

const getProgressColor = (percentage: number) => {
  if (percentage >= 80) return '#10b981'
  if (percentage >= 50) return '#3b82f6'
  return '#f59e0b'
}

const getStatusType = (status: string) => {
  const map: Record<string, any> = {
    COMPLETED: 'success',
    IN_PROGRESS: 'warning',
    NOT_STARTED: 'info'
  }
  return map[status] || 'info'
}

const getStatusText = (status: string) => {
  const map: Record<string, string> = {
    COMPLETED: '已完成',
    IN_PROGRESS: '学习中',
    NOT_STARTED: '未开始'
  }
  return map[status] || status
}

const formatDate = (date: string) => {
  return new Date(date).toLocaleString('zh-CN')
}

const continueLearning = (row: LearningHistory) => {
  router.push({
    name: 'Learning',
    params: { courseId: row.courseId }
  })
}

const reviewCourse = (row: LearningHistory) => {
  router.push({
    name: 'CourseDetail',
    params: { id: row.courseId }
  })
}

const loadHistory = async () => {
  loading.value = true
  try {
    // TODO: 调用真实 API
    await new Promise(resolve => setTimeout(resolve, 500))
    // Mock data
    historyList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadHistory()
})
</script>

<style scoped>
.learning-history {
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 24px;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
  color: #1e293b;
  margin: 0;
}

.history-card {
  border-radius: 16px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>
