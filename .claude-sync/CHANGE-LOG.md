# 变更日志

## [2026-03-02]

### 新增

- [R01] 项目初始化
- [R01] 创建协作文件结构
- [R01] 系统架构设计文档 (`docs/architecture.md`)
- [R01] API 接口规范文档 (`docs/api-spec.md`)
- [R01] 数据库设计文档 (`docs/database-design.md`)
- [R01] 部署计划文档 (`docs/deployment-plan.md`)
- [R05] AI 服务框架 (FastAPI)
  - `ai-service/app/main.py` - FastAPI 应用入口
  - `ai-service/app/config.py` - 配置管理
  - `ai-service/app/api/health.py` - 健康检查接口
  - `ai-service/app/api/qa.py` - AI 问答接口
  - `ai-service/app/services/llm_service.py` - 通义千问集成 (支持 Mock 模式)
  - `ai-service/app/services/conversation_service.py` - 对话历史管理 (内存存储)
  - `ai-service/app/models/__init__.py` - 数据模型
  - `ai-service/app/utils/__init__.py` - 工具函数
  - `ai-service/requirements.txt` - Python 依赖
  - `ai-service/.env.example` - 环境变量模板
  - `ai-service/README.md` - AI 服务文档
- [R06] 部署配置文件
  - `backend/Dockerfile` - 后端 Spring Boot Docker 镜像
  - `backend/.dockerignore` - 后端构建优化
  - `ai-service/Dockerfile` - AI 服务 FastAPI Docker 镜像
  - `ai-service/.dockerignore` - AI 服务构建优化
  - `railway.toml` - Railway 部署配置
  - `vercel.json` - Vercel 部署配置
  - `frontend/.env.production` - 前端生产环境变量
  - `frontend/.env.development` - 前端开发环境变量
  - `frontend/.env.example` - 前端环境变量模板
- [R02] 数据库初始化
  - `backend/src/main/resources/schema.sql` - 建表 SQL 脚本 (6 张表)
  - `backend/src/main/resources/data.sql` - 测试数据 SQL 脚本
  - `backend/src/main/resources/application.yml` - 后端配置文件
  - PostgreSQL 数据库 `cleaning_talent_demo` 已创建并初始化
- [R03] 后端核心功能 (Spring Boot)
  - `backend/src/main/java/com/lms/LmsApplication.java` - 主应用类
  - `backend/src/main/java/com/lms/config/` - 配置类 (CORS, Security, JWT, MyBatis-Plus)
  - `backend/src/main/java/com/lms/controller/` - 控制器 (Auth, Course, Learning)
  - `backend/src/main/java/com/lms/service/` - 服务层 (业务逻辑)
  - `backend/src/main/java/com/lms/mapper/` - MyBatis Mapper 接口
  - `backend/src/main/java/com/lms/entity/` - 实体类 (User, Course, Category, Chapter, UserLearningRecord, PointsRecord)
  - `backend/src/main/java/com/lms/dto/` - 数据传输对象 (16 个 DTO)
  - `backend/src/main/java/com/lms/utils/` - 工具类 (JwtUtil)
  - `backend/src/main/java/com/lms/exception/` - 异常处理 (全局异常处理器)
  - `backend/pom.xml` - Maven 依赖配置
  - `backend/README.md` - 后端项目文档
- [R04] 前端项目 (Vue 3 + TypeScript)
  - `frontend/vite.config.ts` - Vite 配置 (含 API 代理)
  - `frontend/tsconfig.app.json` - TypeScript 配置 (含 @/ 路径映射)
  - `frontend/.env` - 环境变量
  - `frontend/src/types/index.ts` - 完整的 TypeScript 类型系统
  - `frontend/src/utils/request.ts` - Axios 实例 (含 Token 刷新逻辑)
  - `frontend/src/api/auth.ts` - 认证接口封装
  - `frontend/src/api/course.ts` - 课程接口封装
  - `frontend/src/api/learning.ts` - 学习接口封装
  - `frontend/src/api/points.ts` - 积分接口封装
  - `frontend/src/api/ai.ts` - AI 接口封装
  - `frontend/src/stores/user.ts` - 用户状态管理 (Pinia)
  - `frontend/src/router/index.ts` - 路由配置 + 认证守卫
  - `frontend/src/layouts/MainLayout.vue` - 主布局 (渐变色侧边栏)
  - `frontend/src/components/CourseCard.vue` - 课程卡片组件
  - `frontend/src/views/Login.vue` - 登录页 (浮动背景动画)
  - `frontend/src/views/Dashboard.vue` - 仪表盘
  - `frontend/src/views/CourseList.vue` - 课程列表 (筛选/搜索/分页)
  - `frontend/src/views/CourseDetail.vue` - 课程详情
  - `frontend/src/views/Learning.vue` - 学习页面
  - `frontend/src/views/LearningHistory.vue` - 学习历史
  - `frontend/src/views/Ranking.vue` - 积分排行榜
  - `frontend/src/views/AIChat.vue` - AI 助手聊天
  - `frontend/src/views/Profile.vue` - 个人中心
  - `frontend/src/views/NotFound.vue` - 404 页面

### 修改

- [R06] 更新 `frontend/vite.config.ts` - 添加环境变量支持和开发代理
- [R06] 更新 `frontend/.gitignore` - 添加环境变量文件忽略规则
- [R02] 创建 `backend/src/main/resources/` 目录结构

### 技术决策

1. **项目启动**：
   - 确定项目名称：cleaning_Talent
   - 确定工作目录：/Users/kjonekong/Documents/个人/cleaning_Talent
   - 确定开发模式：单人 + Claude Code 协作

2. **技术栈选型 (R01)**：
   - 前端: Vue 3 + TypeScript + Vite + Element Plus + Pinia
   - 后端: Spring Boot 3.2 + Java 17 + MyBatis-Plus + Spring Security + JWT
   - AI: FastAPI + Python 3.11 + LangChain + 通义千问
   - 基础设施: PostgreSQL 15 + Redis 7 + MinIO

3. **架构模式 (R01)**：
   - 选择 Modular Monolith (模块化单体)
   - 模块间通过接口通信，禁止跨模块直接访问实体层
   - 为未来微服务化预留接口边界

4. **部署方案 (R01)**：
   - 前端: Vercel (免费)
   - 后端: Railway (免费)
   - AI 服务: Node A (已有服务器)
   - 基础设施: Node A (已有 PostgreSQL, Redis, MinIO)
   - Demo 阶段完全免费

5. **Demo 功能范围 (R01)**：
   - 已实现: Auth, Course, Learning, Points, AI 模块
   - 暂不实现: Talent (人才评价) 模块、实时评估、九宫格

6. **数据库实现 (R02)**：
   - MySQL 语法转换为 PostgreSQL 语法
   - AUTO_INCREMENT → BIGSERIAL
   - 添加自动更新触发器 (updated_at)
   - 添加 CHECK 约束确保数据完整性
   - 支持环境变量覆盖敏感配置

7. **AI 服务实现 (R05)**：
   - 使用 FastAPI 框架搭建 AI 服务
   - 集成通义千问 API (DashScope SDK)
   - 提供 Mock 模式用于开发测试
   - 内存存储对话历史 (Demo 阶段)
   - 统一响应格式 (code, message, data, timestamp)

8. **前端实现 (R04)**：
   - 采用 Vue 3 + TypeScript + Vite 技术栈
   - 使用 Element Plus UI 框架
   - Pinia 状态管理 + Vue Router 路由
   - 深青色 (#0d9488) 主色调，区别于常见蓝色企业应用
   - 金色 (#fbbf24) 积分强调色
   - 渐变色侧边栏 + 非对称仪表盘设计
   - 页面交错过场动画 (fade-slide)
   - Mock 数据开发，后端 API 完成后可直接对接

### 角色状态更新

| 角色 | 状态 |
|------|------|
| R01 架构师 | ✅ 已完成 |
| R02 数据库工程师 | ✅ 已完成 |
| R03 后端工程师 | ✅ 已完成 |
| R04 前端工程师 | ✅ 已完成 |
| R05 AI 工程师 | ✅ 已完成 |
| R06 DevOps | ✅ 已完成 |

### 已知问题

- 无

---

## [变更类型说明]

- **新增**：新文件、新功能
- **修改**：现有功能变更
- **删除**：移除文件或功能
- **重构**：代码重构不改变功能
- **修复**：Bug 修复
- **优化**：性能或体验优化
