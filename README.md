# cleaning_Talent

企业学习与人才管理一体化平台 - Demo/POC

## 项目简介

cleaning_Talent 是一个面向企业的学习与人才管理一体化平台，整合了在线学习、学习地图、积分系统、AI 问答、人才评价等功能，帮助企业实现培训数字化和人才管理智能化。

**当前阶段**: Demo/POC（概念验证）

## 技术栈

### 前端
- Vue 3 + TypeScript
- Vite（构建工具）
- Element Plus（UI 框架）
- Vue Router（路由）
- Pinia（状态管理）

### 后端
- Spring Boot 3.x
- Java 17
- MyBatis-Plus（ORM）
- Spring Security（安全）
- JWT（身份认证）

### AI 服务
- FastAPI
- Python 3.11
- LangChain
- 通义千问 API

### 基础设施
- PostgreSQL（数据库）
- Redis（缓存）
- MinIO（对象存储）
- Vercel（前端部署）
- Railway（后端部署）

## 快速开始

### 前置要求

- Node.js 18+
- Java 17+
- Python 3.11+
- Docker & Docker Compose（可选）

### 本地开发

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd cleaning_Talent
   ```

2. **启动本地开发环境**
   ```bash
   docker-compose -f docker-compose.dev.yml up -d
   ```

3. **启动后端**
   ```bash
   cd backend
   ./gradlew bootRun
   ```

4. **启动前端**
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

5. **启动 AI 服务**
   ```bash
   cd ai-service
   python -m venv venv
   source venv/bin/activate
   pip install -r requirements.txt
   uvicorn app.main:app --reload
   ```

6. **访问应用**
   - 前端: http://localhost:5173
   - 后端 API: http://localhost:8080
   - AI 服务: http://localhost:8000
   - API 文档: http://localhost:8000/docs

## 项目结构

```
cleaning_Talent/
├── .claude-sync/          # Claude Code 协作文件
│   ├── SESSION-LOG.md      # 会话日志
│   ├── DEPENDENCY-TREE.md  # 依赖关系
│   ├── PROGRESS-BOARD.md   # 进度看板
│   ├── SHARED-CONTEXT.md   # 共享上下文
│   ├── CODE-OWNERSHIP.md   # 代码所有权
│   └── CHANGE-LOG.md       # 变更日志
│
├── docs/                  # 设计文档
│   ├── architecture.md     # 架构设计
│   ├── api-spec.md        # API 规范
│   ├── database-design.md  # 数据库设计
│   └── deployment-plan.md  # 部署计划
│
├── backend/              # 后端项目（Spring Boot）
├── frontend/             # 前端项目（Vue 3）
├── ai-service/           # AI 服务（FastAPI）
├── scripts/              # 工具脚本
│   ├── setup-sync-folder.sh
│   ├── check-status.sh
│   ├── deploy-vercel.sh
│   ├── deploy-railway.sh
│   ├── deploy-to-aliyun.sh
│   └── init-database.sh
│
├── docker-compose.dev.yml # 本地开发环境
├── .env.example          # 环境变量模板
└── README.md             # 本文件
```

## 开发模式

本项目使用 Claude Code 进行单人开发，通过多个会话并行工作：

- **R01 架构师**: 技术选型、架构设计
- **R02 数据库工程师**: 数据库设计、SQL 脚本
- **R03 后端工程师**: 后端业务逻辑、API 实现
- **R04 前端工程师**: 页面开发、组件实现
- **R05 AI 工程师**: AI 功能、模型集成
- **R06 DevOps**: 部署配置、环境搭建

详见 [`.claude-sync/`](./.claude-sync/) 目录。

## 部署

### 前端部署（Vercel）

```bash
bash scripts/deploy-vercel.sh
```

### 后端部署（Railway）

```bash
bash scripts/deploy-railway.sh
```

### AI 服务部署（节点 A）

```bash
bash scripts/deploy-to-aliyun.sh
```

## 环境变量

复制 `.env.example` 到 `.env` 并配置：

```bash
# 数据库
DATABASE_URL=postgresql://postgres:password@localhost:5432/cleaning_talent_demo

# Redis
REDIS_URL=redis://localhost:6379

# MinIO
MINIO_ENDPOINT=localhost:9000
MINIO_ACCESS_KEY=minioadmin
MINIO_SECRET_KEY=minioadmin

# AI 服务
QWEN_API_KEY=your_api_key_here
```

## Demo 功能范围

### 已实现
- 用户认证（登录、登出）
- 课程管理（CRUD）
- 学习记录（进度追踪）
- AI 问答（基础）

### 计划中
- 学习地图
- 积分系统
- 人才评价（九宫格）
- 实时评估

### 暂不实现
- 实时评估（WebRTC）
- 九宫格人才地图
- 复杂报表

## 许可证

MIT License

## 联系方式

- 项目地址: [GitHub URL]
- 问题反馈: [Issues URL]
