import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/stores/user'
import type { RouteMetaCustom } from '@/types'

// 扩展 RouteMeta 类型
declare module 'vue-router' {
  interface RouteMeta extends RouteMetaCustom {}
}

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: {
      title: '登录',
      requiresAuth: false,
      hideInMenu: true
    }
  },
  {
    path: '/',
    component: () => import('@/layouts/MainLayout.vue'),
    meta: {
      requiresAuth: true
    },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: {
          title: '仪表盘',
          icon: 'Odometer'
        }
      },
      {
        path: 'courses',
        name: 'CourseList',
        component: () => import('@/views/CourseList.vue'),
        meta: {
          title: '课程中心',
          icon: 'Reading'
        }
      },
      {
        path: 'courses/:id',
        name: 'CourseDetail',
        component: () => import('@/views/CourseDetail.vue'),
        meta: {
          title: '课程详情',
          hideInMenu: true
        }
      },
      {
        path: 'learning/:courseId',
        name: 'Learning',
        component: () => import('@/views/Learning.vue'),
        meta: {
          title: '学习中心',
          hideInMenu: true
        }
      },
      {
        path: 'history',
        name: 'LearningHistory',
        component: () => import('@/views/LearningHistory.vue'),
        meta: {
          title: '学习历史',
          icon: 'Clock'
        }
      },
      {
        path: 'ranking',
        name: 'Ranking',
        component: () => import('@/views/Ranking.vue'),
        meta: {
          title: '积分排行',
          icon: 'Trophy'
        }
      },
      {
        path: 'ai-chat',
        name: 'AIChat',
        component: () => import('@/views/AIChat.vue'),
        meta: {
          title: 'AI 助手',
          icon: 'ChatDotRound'
        }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/Profile.vue'),
        meta: {
          title: '个人中心',
          icon: 'User'
        }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: {
      title: '页面未找到',
      hideInMenu: true
    }
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// 路由守卫
router.beforeEach((to, _from, next) => {
  const userStore = useUserStore()
  const requiresAuth = to.meta.requiresAuth !== false

  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 企业学习平台`
  }

  // 需要认证但未登录
  if (requiresAuth && !userStore.isLoggedIn) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }

  // 已登录用户访问登录页，重定向到仪表盘
  if (to.name === 'Login' && userStore.isLoggedIn) {
    next({ name: 'Dashboard' })
    return
  }

  next()
})

export default router
