<template>
  <div class="course-list">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">课程中心</h1>
        <p class="page-subtitle">探索知识，提升技能</p>
      </div>
      <div class="header-actions">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索课程..."
          :prefix-icon="Search"
          clearable
          class="search-input"
          @input="handleSearch"
        />
      </div>
    </div>

    <!-- 筛选栏 -->
    <div class="filter-bar">
      <div class="filter-group">
        <span class="filter-label">分类：</span>
        <el-radio-group v-model="filters.category" size="small" @change="handleFilter">
          <el-radio-button :label="0">全部</el-radio-button>
          <el-radio-button
            v-for="cat in categories"
            :key="cat.id"
            :label="cat.id"
          >
            {{ cat.name }}
          </el-radio-button>
        </el-radio-group>
      </div>
      <div class="filter-group">
        <span class="filter-label">难度：</span>
        <el-select v-model="filters.difficulty" placeholder="全部" size="small" @change="handleFilter">
          <el-option label="全部" value="" />
          <el-option label="入门" value="BEGINNER" />
          <el-option label="进阶" value="INTERMEDIATE" />
          <el-option label="高级" value="ADVANCED" />
        </el-select>
      </div>
    </div>

    <!-- 课程列表 -->
    <div v-loading="loading" class="course-grid">
      <CourseCard
        v-for="course in courses"
        :key="course.id"
        :course="course"
      />
    </div>

    <!-- 空状态 -->
    <div v-if="!loading && courses.length === 0" class="empty-state">
      <el-empty description="暂无课程">
        <el-button type="primary" @click="resetFilters">重置筛选</el-button>
      </el-empty>
    </div>

    <!-- 分页 -->
    <div v-if="total > 0" class="pagination">
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="total"
        :page-sizes="[12, 24, 48]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import CourseCard from '@/components/CourseCard.vue'
import type { Course, CourseQuery } from '@/types'

const loading = ref(false)
const searchKeyword = ref('')
const courses = ref<Course[]>([])
const total = ref(0)

const pagination = reactive({
  page: 1,
  size: 12
})

const filters = reactive<CourseQuery>({
  category: 0,
  difficulty: undefined as any
})

const categories = ref([
  { id: 1, name: '前端开发' },
  { id: 2, name: '后端开发' },
  { id: 3, name: '移动开发' },
  { id: 4, name: '数据库' },
  { id: 5, name: '运维测试' },
  { id: 6, name: '人工智能' }
])

const loadCourses = async () => {
  loading.value = true
  try {
    // TODO: 调用真实 API
    // const res = await getCourses({
    //   page: pagination.page,
    //   size: pagination.size,
    //   ...filters
    // })
    // courses.value = res.items
    // total.value = res.total

    // Mock data
    await mockDelay()
    const mockCourses: Course[] = [
      {
        id: 1,
        title: 'Vue 3 快速入门',
        description: '从零开始学习 Vue 3 组合式 API，掌握现代前端开发技能',
        coverUrl: 'https://images.unsplash.com/photo-1633356122544-f134324a6cee?w=400',
        duration: 120,
        difficulty: 'BEGINNER',
        pointsReward: 100,
        category: { id: 1, name: '前端开发' },
        status: 'PUBLISHED',
        createdAt: '2026-03-01T00:00:00Z'
      },
      {
        id: 2,
        title: 'Spring Boot 实战',
        description: '构建企业级 Java 后端应用，掌握微服务架构',
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
        title: 'React 18 新特性解析',
        description: '深入了解 React 18 的并发特性、Suspense 和新 Hooks',
        coverUrl: 'https://images.unsplash.com/photo-1633356122102-7ebdb8b1d8e9?w=400',
        duration: 90,
        difficulty: 'ADVANCED',
        pointsReward: 200,
        category: { id: 1, name: '前端开发' },
        status: 'PUBLISHED',
        createdAt: '2026-03-01T00:00:00Z'
      },
      {
        id: 4,
        title: 'Python 数据分析基础',
        description: '使用 Pandas 和 NumPy 进行数据处理与分析',
        coverUrl: 'https://images.unsplash.com/photo-1526379095098-d400fd0bf935?w=400',
        duration: 150,
        difficulty: 'BEGINNER',
        pointsReward: 120,
        category: { id: 6, name: '人工智能' },
        status: 'PUBLISHED',
        createdAt: '2026-03-01T00:00:00Z'
      },
      {
        id: 5,
        title: 'Docker 容器化部署',
        description: '掌握 Docker 容器技术，实现应用快速部署',
        coverUrl: 'https://images.unsplash.com/photo-1605745341112-85968b19335b?w=400',
        duration: 80,
        difficulty: 'BEGINNER',
        pointsReward: 80,
        category: { id: 5, name: '运维测试' },
        status: 'PUBLISHED',
        createdAt: '2026-03-01T00:00:00Z'
      },
      {
        id: 6,
        title: 'TypeScript 进阶教程',
        description: '深入理解 TypeScript 类型系统与高级特性',
        coverUrl: 'https://images.unsplash.com/photo-1516116216624-53e697fedbea?w=400',
        duration: 140,
        difficulty: 'ADVANCED',
        pointsReward: 180,
        category: { id: 1, name: '前端开发' },
        status: 'PUBLISHED',
        createdAt: '2026-03-01T00:00:00Z'
      }
    ]

    // Apply filters
    let filtered = mockCourses
    if (filters.category) {
      filtered = filtered.filter(c => c.category.id === filters.category)
    }
    if (filters.difficulty) {
      filtered = filtered.filter(c => c.difficulty === filters.difficulty)
    }
    if (searchKeyword.value) {
      const keyword = searchKeyword.value.toLowerCase()
      filtered = filtered.filter(c =>
        c.title.toLowerCase().includes(keyword) ||
        c.description.toLowerCase().includes(keyword)
      )
    }

    total.value = filtered.length
    courses.value = filtered.slice(
      (pagination.page - 1) * pagination.size,
      pagination.page * pagination.size
    )
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadCourses()
}

const handleFilter = () => {
  pagination.page = 1
  loadCourses()
}

const handlePageChange = (page: number) => {
  pagination.page = page
  loadCourses()
  // Scroll to top
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const handleSizeChange = (size: number) => {
  pagination.size = size
  pagination.page = 1
  loadCourses()
}

const resetFilters = () => {
  filters.category = 0
  filters.difficulty = undefined
  searchKeyword.value = ''
  pagination.page = 1
  loadCourses()
}

const mockDelay = () => new Promise(resolve => setTimeout(resolve, 300))

onMounted(() => {
  loadCourses()
})
</script>

<style scoped>
.course-list {
  max-width: 1400px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
  gap: 24px;
}

.header-content {
  flex: 1;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 8px 0;
}

.page-subtitle {
  font-size: 14px;
  color: #64748b;
  margin: 0;
}

.search-input {
  width: 300px;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 24px;
}

/* 筛选栏 */
.filter-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 24px;
  padding: 20px;
  background: #fff;
  border-radius: 16px;
  margin-bottom: 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 12px;
}

.filter-label {
  font-size: 14px;
  font-weight: 500;
  color: #475569;
}

/* 课程网格 */
.course-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 32px;
  min-height: 200px;
}

/* 空状态 */
.empty-state {
  padding: 60px 0;
  text-align: center;
}

/* 分页 */
.pagination {
  display: flex;
  justify-content: center;
  padding: 24px 0;
}

/* 响应式 */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
  }

  .search-input {
    width: 100%;
  }

  .filter-bar {
    flex-direction: column;
    gap: 16px;
  }

  .filter-group {
    flex-direction: column;
    align-items: flex-start;
  }

  .course-grid {
    grid-template-columns: 1fr;
  }
}
</style>
