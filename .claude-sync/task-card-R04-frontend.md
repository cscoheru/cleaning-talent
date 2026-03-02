# 📋 任务卡片：R04 前端工程师

## 角色信息

| 字段 | 值 |
|------|-----|
| **角色编号** | R04 |
| **角色名称** | 前端工程师 |
| **主要职责** | 页面开发、组件实现、前后端联调 |
| **工作时长** | 4-6 小时 |
| **开始条件** | 等待 R01 完成 API 规范 |

---

## 会话目标

搭建前端项目框架，实现核心页面和组件，完成与后端 API 的对接。

---

## 依赖检查

**开始前请确认**：

```bash
# 检查 R01 API 规范是否完成
cat docs/api-spec.md

# 检查是否有 API 接口可以对接
# 如果 R03 未完成，可以先使用 Mock 数据开发
```

---

## 需要读取的文件

```bash
# API 规范
cat docs/api-spec.md

# 系统架构
cat docs/architecture.md

# 共享上下文
cat .claude-sync/SHARED-CONTEXT.md
```

---

## 任务清单

### 第一阶段：项目初始化（1小时）

- [ ] 创建 Vue 3 项目
  ```bash
  cd frontend
  npm create vite@latest . -- --template vue-ts
  npm install
  ```

- [ ] 安装依赖
  ```bash
  npm install element-plus             # UI 框架
  npm install vue-router               # 路由
  npm install pinia                    # 状态管理
  npm install axios                    # HTTP 客户端
  npm install @element-plus/icons-vue  # 图标
  ```

- [ ] 配置 Vite (vite.config.ts)
- [ ] 配置 ESLint、Prettier
- [ ] 配置 Element Plus (按需引入)

### 第二阶段：基础架构（1小时）

- [ ] 创建目录结构
  ```
  frontend/src/
  ├── api/              # API 接口封装
  ├── assets/           # 静态资源
  ├── components/       # 公共组件
  ├── views/            # 页面组件
  ├── router/           # 路由配置
  ├── stores/           # 状态管理
  ├── utils/            # 工具函数
  ├── types/            # TypeScript 类型
  └── App.vue
  ```

- [ ] 配置路由
  ```typescript
  const routes = [
    { path: '/', redirect: '/dashboard' },
    { path: '/login', component: Login },
    { path: '/dashboard', component: Dashboard },
    { path: '/courses', component: CourseList },
    { path: '/courses/:id', component: CourseDetail },
    { path: '/learning', component: Learning },
  ]
  ```

- [ ] 配置 Axios 拦截器
  - 请求拦截器：添加 Token
  - 响应拦截器：统一错误处理

- [ ] 创建基础 Layout 组件
  - 侧边栏导航
  - 顶部导航
  - 内容区域

### 第三阶段：核心页面（2小时）

- [ ] 登录页面 (views/Login.vue)
  - 用户名/密码输入
  - 记住我选项
  - 登录按钮
  - 错误提示

- [ ] 仪表盘页面 (views/Dashboard.vue)
  - 欢迎信息
  - 学习进度概览
  - 推荐课程

- [ ] 课程列表页 (views/CourseList.vue)
  - 课程卡片
  - 筛选/搜索
  - 分页

- [ ] 课程详情页 (views/CourseDetail.vue)
  - 课程信息
  - 目录列表
  - 开始学习按钮

- [ ] 学习页面 (views/Learning.vue)
  - 视频/文档播放器
  - 进度显示
  - 下一节按钮

### 第四阶段：公共组件（1小时）

- [ ] CourseCard.vue - 课程卡片
- [ ] ProgressBar.vue - 进度条
- [ ] EmptyState.vue - 空状态
- [ ] LoadingSpinner.vue - 加载中

### 第五阶段：API 对接（1小时）

- [ ] 创建 API 封装
  ```typescript
  // api/auth.ts
  export const login = (username: string, password: string) => {
    return request.post('/api/v1/auth/login', { username, password })
  }

  // api/course.ts
  export const getCourses = (page: number, size: number) => {
    return request.get('/api/v1/courses', { params: { page, size } })
  }
  ```

- [ ] 创建 Pinia Store
  ```typescript
  // stores/user.ts
  export const useUserStore = defineStore('user', () => {
    const token = ref('')
    const userInfo = ref(null)

    const login = async (username, password) => {
      const res = await authApi.login(username, password)
      token.value = res.token
      userInfo.value = res.userInfo
    }

    return { token, userInfo, login }
  })
  ```

---

## 输出文件

### 核心文件

1. **`frontend/package.json`** - 依赖配置
2. **`frontend/vite.config.ts`** - Vite 配置
3. **`frontend/src/router/index.ts`** - 路由配置
4. **`frontend/src/stores/index.ts`** - 状态管理
5. **`frontend/src/api/`** - API 封装
6. **`frontend/src/views/`** - 页面组件
7. **`frontend/src/components/`** - 公共组件
8. **`frontend/src/layouts/`** - 布局组件

### 代码示例

#### API 封装示例

```typescript
// src/utils/request.ts
import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080',
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const { code, message, data } = response.data
    if (code === 200) {
      return data
    } else {
      ElMessage.error(message || '请求失败')
      return Promise.reject(new Error(message))
    }
  },
  error => {
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default request
```

#### 登录页面示例

```vue
<!-- src/views/Login.vue -->
<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>企业学习平台</h2>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width: 100%">
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const form = ref({ username: '', password: '' })
const loading = ref(false)

const handleLogin = async () => {
  loading.value = true
  try {
    await userStore.login(form.value.username, form.value.password)
    router.push('/dashboard')
  } finally {
    loading.value = false
  }
}
</script>
```

---

## 完成后要更新的文件

```bash
# 1. 更新进度看板
# 编辑 .claude-sync/PROGRESS-BOARD.md

# 2. 更新代码所有权
# 编辑 .claude-sync/CODE-OWNERSHIP.md
# 声明 /frontend/src/ 为 R04 负责

# 3. 更新会话日志
# 编辑 .claude-sync/SESSION-LOG.md

# 4. 更新变更日志
# 编辑 .claude-sync/CHANGE-LOG.md
```

---

## 重要提醒

⚠️ **开发服务器地址**：
- 开发环境: `http://localhost:5173`
- API 地址: `http://localhost:8080`（后端本地）

⚠️ **Mock 数据**：
- 如果 R03 后端未完成，可以先使用 Mock 数据开发 UI
- Mock 数据放在 `frontend/src/mocks/` 目录

⚠️ **样式约定**：
- 使用 Element Plus 组件
- 遵循 Element Plus 设计规范
- 颜色主题待定（可先用默认蓝色）

---

## 开发流程

1. **启动开发服务器**
   ```bash
   cd frontend
   npm run dev
   ```

2. **构建生产版本**
   ```bash
   npm run build
   ```

3. **提交代码**
   ```bash
   git add .
   git commit -m "feat(R04): 实现登录页面和基础布局"
   ```

---

## 验收标准

完成本会话后，应该能够：
- [ ] 前端项目可以在本地启动
- [ ] 登录页面可以正常显示
- [ ] 可以输入用户名密码并点击登录
- [ ] 课程列表页面可以正常显示
- [ ] Layout 组件包含侧边栏和顶部导航
- [ ] 可以使用 Mock 数据或真实 API 数据
