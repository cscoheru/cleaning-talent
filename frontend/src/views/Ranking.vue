<template>
  <div class="ranking">
    <div class="page-header">
      <h1 class="page-title">积分排行榜</h1>
      <p class="page-subtitle">看看谁是学习之星</p>
    </div>

    <el-row :gutter="24">
      <!-- 前三名 -->
      <el-col :span="24">
        <div class="top-three">
          <div
            v-for="(item, index) in topThree"
            :key="item.userId"
            class="top-item"
            :class="`rank-${index + 1}`"
          >
            <div class="rank-badge">
              <el-icon :size="32"><Trophy /></el-icon>
            </div>
            <el-avatar :size="64" :src="item.avatarUrl">
              <el-icon :size="32"><User /></el-icon>
            </el-avatar>
            <div class="user-info">
              <p class="user-name">{{ item.userName }}</p>
              <p class="user-points">{{ item.points }} 积分</p>
            </div>
            <div class="rank-number">{{ index + 1 }}</div>
          </div>
        </div>
      </el-col>

      <!-- 完整榜单 -->
      <el-col :span="24">
        <el-card v-loading="loading" class="ranking-card">
          <el-table :data="rankingList" stripe>
            <el-table-column label="排名" width="80" align="center">
              <template #default="{ $index }">
                <span v-if="$index < 3" class="rank-icon">
                  <el-icon :size="20"><Medal /></el-icon>
                </span>
                <span v-else class="rank-number">{{ $index + 4 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="用户" min-width="200">
              <template #default="{ row }">
                <div class="user-cell">
                  <el-avatar :size="40" :src="row.avatarUrl">
                    <el-icon><User /></el-icon>
                  </el-avatar>
                  <span class="user-name">{{ row.userName }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="积分" width="150">
              <template #default="{ row }">
                <div class="points-cell">
                  <el-icon color="#fbbf24"><Coin /></el-icon>
                  <span>{{ row.points }}</span>
                </div>
              </template>
            </el-table-column>
          </el-table>

          <div class="pagination">
            <el-pagination
              v-model:current-page="pagination.page"
              v-model:page-size="pagination.size"
              :total="total"
              layout="total, sizes, prev, pager, next"
              @size-change="loadRanking"
              @current-change="loadRanking"
            />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { Trophy, User, Medal, Coin } from '@element-plus/icons-vue'
import type { RankingItem } from '@/types'

const loading = ref(false)
const rankingList = ref<RankingItem[]>([])
const total = ref(0)

const pagination = reactive({
  page: 1,
  size: 20
})

const topThree = computed(() => rankingList.value.slice(0, 3))

const loadRanking = async () => {
  loading.value = true
  try {
    // TODO: 调用真实 API
    await new Promise(resolve => setTimeout(resolve, 500))
    // Mock data
    rankingList.value = [
      { rank: 1, userId: 1, userName: '张三', avatarUrl: '', points: 2500 },
      { rank: 2, userId: 2, userName: '李四', avatarUrl: '', points: 2300 },
      { rank: 3, userId: 3, userName: '王五', avatarUrl: '', points: 2100 },
      { rank: 4, userId: 4, userName: '赵六', avatarUrl: '', points: 1900 },
      { rank: 5, userId: 5, userName: '孙七', avatarUrl: '', points: 1750 }
    ]
    total.value = 50
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadRanking()
})
</script>

<style scoped>
.ranking {
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 8px 0;
}

.page-subtitle {
  font-size: 16px;
  color: #64748b;
  margin: 0;
}

/* 前三名 */
.top-three {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  margin-bottom: 40px;
}

.top-item {
  position: relative;
  padding: 32px 20px;
  background: #fff;
  border-radius: 20px;
  text-align: center;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.top-item:hover {
  transform: translateY(-8px);
}

.top-item.rank-1 {
  background: linear-gradient(135deg, #fbbf24 0%, #f59e0b 100%);
  transform: scale(1.05);
}

.top-item.rank-1:hover {
  transform: scale(1.05) translateY(-8px);
}

.top-item.rank-2 {
  background: linear-gradient(135deg, #e2e8f0 0%, #cbd5e1 100%);
}

.top-item.rank-3 {
  background: linear-gradient(135deg, #fde68a 0%, #fcd34d 100%);
}

.rank-badge {
  margin-bottom: 16px;
}

.top-item.rank-1 .rank-badge {
  color: #fff;
}

.top-item:not(.rank-1) .rank-badge {
  color: #94a3b8;
}

.user-info {
  margin-top: 16px;
}

.user-name {
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 8px 0;
}

.user-points {
  font-size: 14px;
  color: #64748b;
  margin: 0;
}

.top-item.rank-1 .user-name,
.top-item.rank-1 .user-points {
  color: #fff;
}

.rank-number {
  position: absolute;
  top: 16px;
  right: 16px;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  font-size: 16px;
  font-weight: 700;
  color: #1e293b;
}

/* 榜单表格 */
.ranking-card {
  border-radius: 16px;
}

.rank-icon {
  color: #fbbf24;
  font-size: 20px;
}

.user-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-cell .user-name {
  font-weight: 500;
  color: #334155;
}

.points-cell {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  color: #f59e0b;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}

/* 响应式 */
@media (max-width: 768px) {
  .top-three {
    grid-template-columns: 1fr;
  }

  .top-item.rank-1 {
    order: -1;
  }
}
</style>
