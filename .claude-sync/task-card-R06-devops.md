# 📋 任务卡片：R06 DevOps

## 角色信息

| 字段 | 值 |
|------|-----|
| **角色编号** | R06 |
| **角色名称** | DevOps 工程师 |
| **主要职责** | 部署配置、环境搭建、CI/CD |
| **工作时长** | 3-4 小时 |
| **开始条件** | 等待 R01 完成部署架构设计 |

---

## 会话目标

配置开发环境和生产环境的部署方案，确保项目可以顺利部署到各个平台。

---

## 依赖检查

**开始前请确认**：

```bash
# 检查 R01 部署计划
cat docs/deployment-plan.md

# 检查现有基础设施信息
cat .claude-sync/SHARED-CONTEXT.md | grep -A 10 "部署信息"
```

---

## 需要读取的文件

```bash
# 部署计划
cat docs/deployment-plan.md

# 现有基础设施
cat /Users/kjonekong/infrastructure/INFRASTRUCTURE.md
```

---

## 任务清单

### 第一阶段：本地开发环境（1小时）

- [ ] 创建 Docker Compose 配置
  ```yaml
  # docker-compose.dev.yml
  version: '3.8'
  services:
    postgres:
      image: postgres:15
      environment:
        POSTGRES_PASSWORD: dev_password
        POSTGRES_DB: cleaning_talent_demo
      ports:
        - "5432:5432"
      volumes:
        - postgres_data:/var/lib/postgresql/data

    redis:
      image: redis:7
      ports:
        - "6379:6379"

    minio:
      image: minio/minio
      command: server /data --console-address ":9001"
      environment:
        MINIO_ROOT_USER: minioadmin
        MINIO_ROOT_PASSWORD: minioadmin
      ports:
        - "9000:9000"
        - "9001:9001"

  volumes:
    postgres_data:
  ```

- [ ] 创建环境变量模板
  ```bash
  # .env.example
  # 数据库配置
   DATABASE_URL=postgresql://postgres:dev_password@localhost:5432/cleaning_talent_demo

  # Redis 配置
   REDIS_URL=redis://localhost:6379

  # MinIO 配置
   MINIO_ENDPOINT=localhost:9000
   MINIO_ACCESS_KEY=minioadmin
   MINIO_SECRET_KEY=minioadmin

  # AI 配置
   QWEN_API_KEY=your_api_key_here
  ```

### 第二阶段：前端部署配置（1小时）

- [ ] 配置 Vercel 部署
  ```json
  // vercel.json
  {
    "buildCommand": "cd frontend && npm run build",
    "outputDirectory": "frontend/dist",
    "framework": "vite",
    "env": {
      "VITE_API_URL": "@api_url"
    }
  }
  ```

- [ ] 配置前端环境变量
  ```bash
  # frontend/.env.production
  VITE_API_URL=https://lms-backend.railway.app
  ```

### 第三阶段：后端部署配置（1小时）

- [ ] 配置 Railway 部署
  ```toml
  # railway.toml
  [build]
  builder = "DOCKERFILE"
  dockerfilePath = "backend/Dockerfile"

  [deploy]
  healthcheckPath = "/actuator/health"
  healthcheckTimeout = 300
  restartPolicyType = "ON_FAILURE"
  restartPolicyMaxRetries = 10
  ```

- [ ] 创建后端 Dockerfile
  ```dockerfile
  # backend/Dockerfile
  FROM eclipse-temurin:17-jre-alpine
  WORKDIR /app
  COPY build/libs/*.jar app.jar
  EXPOSE 8080
  ENTRYPOINT ["java", "-jar", "app.jar"]
  ```

### 第四阶段：AI 服务部署（30分钟）

- [ ] 配置节点 A 部署
  - 创建部署脚本（scripts/deploy-to-aliyun.sh 已提供）
  - 配置 MinIO bucket
  - 配置域名访问（可选）

### 第五阶段：监控配置（30分钟）

- [ ] 配置 Uptime Kuma 监控
  - 添加前端监控：https://lms-demo.vercel.app
  - 添加后端监控：https://lms-backend.railway.app
  - 添加 AI 服务监控：http://139.224.42.111:8000

---

## 输出文件

1. **`docker-compose.dev.yml`** - 本地开发环境
2. **`.env.example`** - 环境变量模板
3. **`vercel.json`** - Vercel 配置
4. **`railway.toml`** - Railway 配置
5. **`backend/Dockerfile`** - 后端 Docker 镜像
6. **`ai-service/Dockerfile`** - AI 服务 Docker 镜像
7. **`.gitignore`** - Git 忽略规则

---

## 完成后要更新的文件

```bash
# 1. 更新进度看板
# 编辑 .claude-sync/PROGRESS-BOARD.md

# 2. 更新代码所有权
# 编辑 .claude-sync/CODE-OWNERSHIP.md
# 声明 /scripts/ 和部署配置为 R06 负责

# 3. 更新共享上下文
# 编辑 .claude-sync/SHARED-CONTEXT.md
# 添加部署信息

# 4. 更新会话日志
# 编辑 .claude-sync/SESSION-LOG.md

# 5. 更新变更日志
# 编辑 .claude-sync/CHANGE-LOG.md
```

---

## 重要提醒

⚠️ **现有基础设施**（节点 A）：
- PostgreSQL: 139.224.42.111:5432
- Redis: 139.224.42.111:6379
- MinIO: 139.224.42.111:9000
- Uptime Kuma: 监控面板

⚠️ **部署平台**：
- 前端：Vercel（免费）
- 后端：Railway（500小时免费/月）
- AI 服务：节点 A Docker
- 数据库：节点 A PostgreSQL（已有）

⚠️ **成本估算**：
- Vercel: 免费（静态托管）
- Railway: 免费（500小时/月足够 Demo）
- 节点 A: 已有（无额外成本）
- **总计: ~$5/月**（超出免费额度后）

---

## 部署流程

### 1. 本地开发环境

```bash
# 启动所有服务
docker-compose -f docker-compose.dev.yml up -d

# 查看日志
docker-compose -f docker-compose.dev.yml logs -f

# 停止服务
docker-compose -f docker-compose.dev.yml down
```

### 2. 前端部署 (Vercel)

```bash
# 安装 Vercel CLI
npm install -g vercel

# 部署
cd frontend
vercel --prod

# 或使用脚本
bash scripts/deploy-vercel.sh
```

### 3. 后端部署 (Railway)

```bash
# 安装 Railway CLI
npm install -g @railway/cli

# 登录
railway login

# 部署
cd backend
railway up

# 或使用脚本
bash scripts/deploy-railway.sh
```

### 4. AI 服务部署 (节点 A)

```bash
# 使用部署脚本
bash scripts/deploy-to-aliyun.sh
```

---

## .gitignore 配置

```gitignore
# 环境变量
.env
.env.local
.env.*.local

# 日志
logs/
*.log
npm-debug.log*

# 依赖
node_modules/
venv/
__pycache__/

# 构建产物
dist/
build/
target/
*.jar

# IDE
.idea/
.vscode/
*.iml

# 操作系统
.DS_Store
Thumbs.db

# 测试覆盖率
coverage/
.nyc_output/

# 临时文件
*.tmp
*.temp
```

---

## 验收标准

完成本会话后，应该能够：
- [ ] 本地 Docker Compose 环境可以启动
- [ ] 前端可以部署到 Vercel
- [ ] 后端可以部署到 Railway
- [ ] AI 服务可以部署到节点 A
- [ ] Uptime Kuma 可以监控所有服务
- [ ] 环境变量配置正确
- [ ] 部署脚本可以正常执行
