<template>
  <el-container class="main-layout">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '240px'" class="sidebar">
      <div class="logo-container">
        <transition name="logo-fade">
          <span v-if="!isCollapse" class="logo-text">学习平台</span>
        </transition>
        <el-icon v-if="isCollapse" class="logo-icon"><Reading /></el-icon>
      </div>

      <el-menu
        :default-active="activeRoute"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
        class="sidebar-menu"
      >
        <template v-for="route in menuRoutes" :key="route.name">
          <el-menu-item
            v-if="!route.meta?.hideInMenu"
            :index="route.path"
            :route="{ name: route.name }"
          >
            <el-icon v-if="route.meta?.icon">
              <component :is="route.meta.icon" />
            </el-icon>
            <template #title>{{ route.meta?.title }}</template>
          </el-menu-item>
        </template>
      </el-menu>

      <div class="sidebar-footer">
        <el-button
          :icon="isCollapse ? Expand : Fold"
          circle
          @click="toggleCollapse"
          class="collapse-btn"
        />
      </div>
    </el-aside>

    <!-- 主体区域 -->
    <el-container class="main-container">
      <!-- 顶部导航栏 -->
      <el-header class="header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item
              v-for="item in breadcrumbs"
              :key="item.path"
              :to="{ path: item.path }"
            >
              {{ item.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="header-right">
          <!-- 积分显示 -->
          <div class="points-display">
            <el-icon class="points-icon"><Coin /></el-icon>
            <span class="points-value">{{ userStore.userPoints }}</span>
          </div>

          <!-- 用户下拉菜单 -->
          <el-dropdown @command="handleUserCommand">
            <div class="user-avatar">
              <el-avatar :size="36" :src="userStore.userInfo?.avatarUrl">
                <el-icon><User /></el-icon>
              </el-avatar>
              <span class="user-name">{{ userStore.userName }}</span>
              <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  个人中心
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区域 -->
      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade-slide" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import {
  Fold,
  Expand,
  User,
  ArrowDown,
  SwitchButton,
  Coin,
  Reading
} from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 侧边栏折叠状态
const isCollapse = ref(false)

// 当前激活的路由
const activeRoute = computed(() => {
  // 对于详情页，使用其父级路由作为激活项
  if (route.name === 'CourseDetail') {
    return '/courses'
  }
  if (route.name === 'Learning') {
    return '/courses'
  }
  return route.path
})

// 菜单路由
const menuRoutes = computed(() => {
  const routes = router.getRoutes()
  return routes.filter(
    r => r.path.startsWith('/') &&
      r.meta?.title &&
      !r.meta?.hideInMenu &&
      r.name !== 'Login'
  )
})

// 面包屑
const breadcrumbs = computed(() => {
  const matched = route.matched.filter(r => r.meta?.title)
  return matched.map(r => ({
    path: r.path,
    title: r.meta?.title as string
  }))
})

// 切换侧边栏
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

// 用户操作
const handleUserCommand = async (command: string) => {
  switch (command) {
    case 'profile':
      router.push({ name: 'Profile' })
      break
    case 'logout':
      try {
        await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await userStore.logout()
        router.push({ name: 'Login' })
      } catch {
        // 取消操作
      }
      break
  }
}

// 监听路由变化，恢复侧边栏状态
watch(() => route.name, () => {
  // 小屏幕默认折叠
  if (window.innerWidth < 768) {
    isCollapse.value = true
  }
}, { immediate: true })
</script>

<style scoped>
.main-layout {
  height: 100vh;
}

/* 侧边栏样式 */
.sidebar {
  background: linear-gradient(180deg, #0d9488 0%, #0f766e 100%);
  transition: width 0.3s ease;
  display: flex;
  flex-direction: column;
  border-right: none;
}

.logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  padding: 0 16px;
}

.logo-text {
  font-size: 20px;
  font-weight: 600;
  color: #fff;
  letter-spacing: 1px;
}

.logo-icon {
  font-size: 28px;
  color: #fff;
}

.logo-fade-enter-active,
.logo-fade-leave-active {
  transition: opacity 0.2s ease;
}

.logo-fade-enter-from,
.logo-fade-leave-to {
  opacity: 0;
}

.sidebar-menu {
  flex: 1;
  border: none;
  background: transparent;
  padding: 8px 0;
  overflow-y: auto;
}

.sidebar-menu:not(.el-menu--collapse) {
  width: 240px;
}

.sidebar-menu :deep(.el-menu-item) {
  color: rgba(255, 255, 255, 0.8);
  border-radius: 0;
  margin: 2px 0;
}

.sidebar-menu :deep(.el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
}

.sidebar-menu :deep(.el-menu-item.is-active) {
  background: rgba(255, 255, 255, 0.15);
  color: #fff;
  position: relative;
}

.sidebar-menu :deep(.el-menu-item.is-active::before) {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 4px;
  background: #fbbf24;
}

.sidebar-footer {
  padding: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  display: flex;
  justify-content: center;
}

.collapse-btn {
  background: rgba(255, 255, 255, 0.1);
  border: none;
  color: #fff;
}

.collapse-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

/* 主体容器 */
.main-container {
  display: flex;
  flex-direction: column;
  background: #f8fafc;
}

/* 顶部导航栏 */
.header {
  background: #fff;
  border-bottom: 1px solid #e2e8f0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.header-left :deep(.el-breadcrumb__item) {
  font-size: 14px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 24px;
}

.points-display {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  background: linear-gradient(135deg, #fbbf24 0%, #f59e0b 100%);
  border-radius: 20px;
  color: #fff;
  font-weight: 600;
}

.points-icon {
  font-size: 18px;
}

.points-value {
  font-size: 14px;
}

.user-avatar {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 8px;
  transition: background 0.2s ease;
}

.user-avatar:hover {
  background: #f1f5f9;
}

.user-name {
  font-size: 14px;
  color: #334155;
  font-weight: 500;
}

.dropdown-icon {
  font-size: 12px;
  color: #94a3b8;
}

/* 内容区域 */
.main-content {
  padding: 24px;
  overflow-y: auto;
}

/* 路由过渡动画 */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.3s ease;
}

.fade-slide-enter-from {
  opacity: 0;
  transform: translateY(10px);
}

.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* 响应式 */
@media (max-width: 768px) {
  .header {
    padding: 0 16px;
  }

  .header-left {
    display: none;
  }

  .main-content {
    padding: 16px;
  }

  .user-name {
    display: none;
  }
}
</style>
