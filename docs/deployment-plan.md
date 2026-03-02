# 部署计划文档

## 项目信息

| 字段 | 值 |
|------|-----|
| **文档版本** | v1.0 |
| **创建日期** | 2026-03-02 |
| **创建角色** | R01 架构师 |
| **项目阶段** | Demo/POC |

---

## 1. 部署架构

### 1.1 生产环境

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        Production Deployment                            │
│                                                                         │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │                     Vercel (Frontend)                            │   │
│  │  Vue 3 SPA → CDN + Edge Network → https://lms-demo.vercel.app   │   │
│  └─────────────────────────────────────────────────────────────────┘   │
│                                  │                                     │
│                                  ▼                                     │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │                    Railway (Backend)                             │   │
│  │  Spring Boot JAR → https://lms-backend.railway.app              │   │
│  │  - Free Tier: 512MB RAM, 0.5 CPU                                │   │
│  │  - Auto-deploy from GitHub                                      │   │
│  └─────────────────────────────────────────────────────────────────┘   │
│                                  │                                     │
│                                  ▼                                     │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │                   Node A (AI Service)                            │   │
│  │  FastAPI + Docker → http://139.224.42.111:8000                  │   │
│  │  - Self-hosted on existing server                               │   │
│  │  - Nginx reverse proxy                                          │   │
│  └─────────────────────────────────────────────────────────────────┘   │
│                                  │                                     │
│                                  ▼                                     │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │                   Node A (Infrastructure)                        │   │
│  │  PostgreSQL + Redis + MinIO (已部署)                            │   │
│  │  - 139.224.42.111:5432 (PostgreSQL)                             │   │
│  │  - 139.224.42.111:6379 (Redis)                                  │   │
│  │  - 139.224.42.111:9000 (MinIO)                                  │   │
│  └─────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────┘
```

### 1.2 本地开发

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        Local Development                                │
│                                                                         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐                   │
│  │   Frontend  │  │   Backend   │  │  AI Service │                   │
│  │   :5173     │  │   :8080     │  │   :8000     │                   │
│  │  (Vite Dev) │  │ (Spring)    │  │ (FastAPI)   │                   │
│  └─────────────┘  └─────────────┘  └─────────────┘                   │
│         │                │                │                            │
│         └────────────────┴────────────────┘                            │
│                          │                                             │
│                          ▼                                             │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │              Docker Compose (Infrastructure)                     │   │
│  │  PostgreSQL :5432  Redis :6379  MinIO :9000                     │   │
│  └─────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 2. 部署目标

### 2.1 前端 (Vercel)

| 配置项 | 值 |
|--------|-----|
| 平台 | Vercel |
| 计划 | Hobby (Free) |
| 域名 | lms-demo.vercel.app |
| 构建命令 | `npm run build` |
| 输出目录 | `dist/` |
| 自动部署 | GitHub main 分支推送 |

### 2.2 后端 (Railway)

| 配置项 | 值 |
|--------|-----|
| 平台 | Railway |
| 计划 | Free Tier |
| 域名 | lms-backend.railway.app |
| 构建命令 | `./gradlew build` |
| JAR | `build/libs/*.jar` |
| 自动部署 | GitHub main 分支推送 |

### 2.3 AI 服务 (Node A)

| 配置项 | 值 |
|--------|-----|
| 平台 | Self-hosted (Docker) |
| 服务器 | 139.224.42.111 |
| 端口 | 8000 |
| 反向代理 | Nginx |

### 2.4 基础设施 (Node A)

| 服务 | 地址 | 用途 |
|------|------|------|
| PostgreSQL | 139.224.42.111:5432 | 主数据库 |
| Redis | 139.224.42.111:6379 | 缓存 |
| MinIO | 139.224.42.111:9000 | 对象存储 |

---

## 3. 网络配置

### 3.1 Railway → Node A 连通性

**要求：** Railway 云端需要能访问 Node A 的数据库服务

**配置步骤：**

1. **PostgreSQL 远程访问配置**
   ```bash
   # /etc/postgresql/pg_hba.conf
   host    all             all             railway_ip/32      md5
   ```

2. **防火墙规则**
   ```bash
   # 允许 Railway IP 访问
   # 具体 IP 需要在 Railway 控制台查看
   ```

3. **验证连接**
   ```bash
   # 从 Railway 连接测试
   psql -h 139.224.42.111 -U postgres -d cleaning_talent_demo
   ```

### 3.2 Node A 端口开放

| 端口 | 服务 | 用途 |
|------|------|------|
| 5432 | PostgreSQL | 数据库 |
| 6379 | Redis | 缓存 |
| 9000 | MinIO | 对象存储 |
| 9001 | MinIO Console | 管理界面 |
| 8000 | FastAPI | AI 服务 |

---

## 4. 环境变量

### 4.1 前端 (Vercel)

```bash
# API endpoints
VITE_API_BASE_URL=https://lms-backend.railway.app
VITE_AI_SERVICE_URL=http://139.224.42.111:8000

# App config
VITE_APP_NAME=cleaning_Talent
```

### 4.2 后端 (Railway)

```bash
# Database (Node A)
DATABASE_URL=postgresql://postgres:WhjQTPAwInc5Vav3sDWe@139.224.42.111:5432/cleaning_talent_demo

# Redis (Node A)
REDIS_URL=redis://:FWD4D75OKyQS7HOluA6J@139.224.42.111:6379

# MinIO (Node A)
MINIO_ENDPOINT=139.224.42.111:9000
MINIO_ACCESS_KEY=admin
MINIO_SECRET_KEY=xhOSMeHNmxCgNTBpoQfH
MINIO_BUCKET=cleaning-talent-demo

# JWT
JWT_SECRET=CHANGE_THIS_IN_PRODUCTION
JWT_EXPIRATION=604800

# CORS
ALLOWED_ORIGINS=https://lms-demo.vercel.app,http://localhost:5173
```

### 4.3 AI 服务 (Node A)

```bash
# Qwen API
QWEN_API_KEY=your_qwen_api_key_here

# Database (for logging, optional)
DATABASE_URL=postgresql://postgres:WhjQTPAwInc5Vav3sDWe@139.224.42.111:5432/cleaning_talent_demo

# CORS
ALLOWED_ORIGINS=https://lms-demo.vercel.app,https://lms-backend.railway.app,http://localhost:5173,http://localhost:8080
```

### 4.4 环境变量文件

| 文件 | 用途 | 提交 |
|------|------|------|
| `.env.example` | 环境变量模板 | ✅ 是 |
| `.env.local` | 本地开发 | ❌ 否 |
| `.env.production` | 生产环境 | ❌ 否 |

---

## 5. CORS 白名单

### 5.1 后端 CORS

```
ALLOWED_ORIGINS=https://lms-demo.vercel.app,http://localhost:5173
```

### 5.2 AI 服务 CORS

```
ALLOWED_ORIGINS=https://lms-demo.vercel.app,https://lms-backend.railway.app,http://localhost:5173,http://localhost:8080
```

---

## 6. CI/CD 流程

### 6.1 前端自动部署 (Vercel)

```
GitHub: push to main branch
         ↓
Vercel: 检测推送
         ↓
Vercel: 自动构建 (npm run build)
         ↓
Vercel: 部署到 CDN
         ↓
完成: https://lms-demo.vercel.app
```

### 6.2 后端自动部署 (Railway)

```
GitHub: push to main branch
         ↓
Railway: 检测推送
         ↓
Railway: 构建 (./gradlew build)
         ↓
Railway: 部署 JAR
         ↓
完成: https://lms-backend.railway.app
```

### 6.3 AI 服务部署 (手动)

```bash
# SSH 到 Node A
ssh root@139.224.42.111

# 拉取最新代码
cd /opt/lms-demo/ai-service
git pull origin main

# 重新构建并启动
docker-compose up -d --build ai-service

# 查看日志
docker-compose logs -f ai-service
```

---

## 7. 成本估算 (Demo 阶段)

| 服务 | 计划 | 月费用 | 年费用 |
|------|------|--------|--------|
| **Vercel** | Hobby | Free | Free |
| **Railway** | Free Tier | Free | Free |
| **Node A** | 已有服务器 | ¥0 | ¥0 |
| **PostgreSQL/Redis/MinIO** | 自托管 | ¥0 | ¥0 |
| **Domain** | vercel.app | Free | Free |
| **Total** | - | **¥0** | **¥0** |

**Demo 阶段完全免费！** 🎉

---

## 8. 部署检查清单

### 8.1 前端部署

- [ ] GitHub 仓库已连接到 Vercel
- [ ] 构建命令配置正确 (`npm run build`)
- [ ] 环境变量已配置
- [ ] 域名已分配
- [ ] 部署成功访问

### 8.2 后端部署

- [ ] GitHub 仓库已连接到 Railway
- [ ] 构建命令配置正确 (`./gradlew build`)
- [ ] 环境变量已配置
- [ ] 数据库连接测试通过
- [ ] 部署成功访问

### 8.3 AI 服务部署

- [ ] Node A 服务器可访问
- [ ] Docker 已安装
- [ ] 环境变量已配置
- [ ] Nginx 反向代理已配置
- [ ] 服务启动成功

### 8.4 网络连通性

- [ ] Railway → Node A 数据库可连接
- [ ] 前端 → 后端 API 可调用
- [ ] 后端 → AI 服务可调用
- [ ] CORS 配置正确

---

## 9. 回滚计划

### 9.1 前端回滚

```bash
# Vercel 控制台选择之前的部署版本
# 或使用 CLI
vercel rollback [deployment-url]
```

### 9.2 后端回滚

```bash
# Railway 控制台选择之前的部署版本
# 或重新部署之前的 commit
git revert HEAD
git push
```

### 9.3 AI 服务回滚

```bash
# SSH 到 Node A
cd /opt/lms-demo/ai-service
git checkout [previous-commit]
docker-compose up -d --build ai-service
```

---

## 10. 监控与日志

### 10.1 Vercel

- 访问日志：Vercel Dashboard
- 错误追踪：Vercel Analytics

### 10.2 Railway

- 访问日志：Railway Dashboard
- 应用日志：`railway logs`

### 10.3 Node A

- 应用日志：`docker-compose logs -f ai-service`
- Nginx 日志：`/var/log/nginx/`
- 系统监控：`htop`

---

*文档创建者: R01 架构师*
*最后更新: 2026-03-02*
