import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  // 环境变量前缀（默认是 VITE_）
  envPrefix: 'VITE_',
  // 开发服务器配置
  server: {
    port: 5173,
    host: true,
    // 使用线上后端，无需本地代理
    // 如需本地开发，启动本地后端并修改 .env.development
  },
  // 构建配置
  build: {
    outDir: 'dist',
    sourcemap: false,
    // chunk 警告阈值
    chunkSizeWarningLimit: 1000,
  },
})
