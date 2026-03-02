# 系统架构设计文档

## 项目信息

| 字段 | 值 |
|------|-----|
| **文档版本** | v1.0 |
| **创建日期** | 2026-03-02 |
| **创建角色** | R01 架构师 |
| **项目阶段** | Demo/POC |

---

## 1. 技术栈

### 1.1 前端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue 3 | 3.4+ | 渐进式 JavaScript 框架 |
| TypeScript | 5.x | 类型安全 |
| Vite | 5.x | 构建工具，快速 HMR |
| Element Plus | Latest | 企业级 UI 组件库 |
| Vue Router | 4.x | 官方路由 |
| Pinia | Latest | 官方状态管理 |

### 1.2 后端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2+ | 企业级 Java 框架 |
| Java | 17 | LTS 版本 |
| MyBatis-Plus | 3.5+ | ORM 框架 |
| Spring Security | 6.x | 安全框架 |
| JWT | - | Token 认证 |

### 1.3 AI 服务技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| FastAPI | 0.100+ | 现代 Python Web 框架 |
| Python | 3.11+ | AI 生态支持 |
| LangChain | Latest | LLM 编排框架 |
| 通义千问 API | - | AI 模型服务 |

### 1.4 基础设施

| 组件 | 版本 | 用途 |
|------|------|------|
| PostgreSQL | 15+ | 主数据库 |
| Redis | 7.x | 缓存、会话 |
| MinIO | Latest | 对象存储 |

---

## 2. 系统分层架构

```
┌─────────────────────────────────────────────────────────────────┐
│                         Presentation Layer                        │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐  │
│  │   Vue 3 SPA     │  │   Mobile (未来)  │  │   Admin Portal  │  │
│  │   (Vercel)      │  │   (预留)        │  │   (未来)         │  │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Business Service Layer                         │
│              (Modular Monolith - Single Spring Boot)              │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐           │
│  │   Auth   │ │  Course  │ │ Learning │ │  Points  │  Modules  │
│  │  Module  │ │  Module  │ │  Module  │ │  Module  │           │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘           │
│  ┌──────────┐ ┌──────────┐                                     │
│  │    AI    │ │  Talent  │                                     │
│  │  Module  │ │  Module  │  (Interface Boundary)               │
│  └──────────┘ └──────────┘                                     │
│                                                                  │
│  ⚠️ Rule: Modules communicate via interfaces only                │
│  ⚠️ Rule: No cross-module direct entity access                   │
└─────────────────────────────────────────────────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────┐
│                       Data Access Layer                           │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │  MyBatis-Plus Repository Pattern                           │ │
│  │  - BaseRepository (CRUD)                                    │ │
│  │  - Custom Repositories (complex queries)                    │ │
│  └────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Infrastructure Layer                         │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐           │
│  │PostgreSQL│ │  Redis   │ │  MinIO   │ │  Qwen    │  External │
│  │  (主库)  │ │  (缓存)  │ │  (存储)  │ │  (AI)    │  Services │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘           │
└─────────────────────────────────────────────────────────────────┘
```

### 2.1 模块化单体架构

Demo 阶段采用 **模块化单体（Modular Monolith）** 架构：

- **单部署单元**：一个 Spring Boot JAR 包
- **模块边界**：清晰的模块接口定义
- **接口通信**：模块间通过接口通信，禁止跨模块直接访问实体层
- **未来扩展**：为微服务化预留接口边界

### 2.2 模块目录结构

```
backend/
├── src/main/java/com/cleaningtalent/
│   ├── shared/                    # 共享层（所有模块可用）
│   │   ├── domain/                # 共享值对象、工具类
│   │   ├── infrastructure/        # 基础设施配置
│   │   └── application/           # 跨应用服务
│   │
│   ├── auth/                      # 认证模块
│   │   ├── domain/                # 实体、值对象
│   │   ├── application/           # 服务接口
│   │   ├── infrastructure/        # 数据访问实现
│   │   └── interfaces/            # REST Controller
│   │
│   ├── course/                    # 课程模块
│   ├── learning/                  # 学习模块
│   ├── points/                    # 积分模块
│   ├── ai/                        # AI模块
│   └── talent/                    # 人才模块（Demo 暂不实现）
```

---

## 3. 模块设计

### 3.1 模块依赖关系

```
      ┌─────┐     ┌─────────┐     ┌──────────┐     ┌──────────┐
      │Auth │────▶│  Course │     │ Learning │     │  Points  │
      │(核心)│    │          │     │          │     │          │
      └─────┘     └─────────┘     └──────────┘     └──────────┘
           │            │              │                │
           │            └──────────────┴────────────────┘
           │                              │
           │           (依赖 User ID 进行业务操作)
           │
           ▼
      ┌─────────────────────┐
      │  Shared Domain      │
      │  - User (ID only)   │
      │  - BaseEntity       │
      └─────────────────────┘

    ┌──────────────────────────────────────────────────────────────┐
    │                    AI Module (Independent)                    │
    │  - 调用外部 Qwen API                                           │
    │  - 暴露 REST API 供其他模块调用                                │
    └──────────────────────────────────────────────────────────────┘
```

### 3.2 核心模块说明

| 模块 | 职责 | 状态 |
|------|------|------|
| **Auth** | 用户认证、授权、Token 管理 | Demo 实现 |
| **Course** | 课程 CRUD、分类管理、章节管理 | Demo 实现 |
| **Learning** | 学习记录、进度追踪、完成状态 | Demo 实现 |
| **Points** | 积分规则、积分记录、排行榜 | Demo 实现 |
| **AI** | AI 问答、学习推荐 | Demo 实现 |
| **Talent** | 人才评价、九宫格 | Demo 暂不实现 |

---

## 4. 模块间通信规则

### 4.1 接口依赖原则

```java
// ✅ 正确：依赖接口
@Autowired
private CourseService courseService;  // Interface

// ❌ 错误：跨模块直接访问 Repository
@Autowired
private CourseRepository courseRepository;  // 禁止！
```

### 4.2 DTO 传输原则

```java
// ✅ 正确：使用 DTO 跨模块传输数据
CourseDTO course = courseService.getCourseById(courseId);
```

### 4.3 模块依赖关系

- 所有业务模块 → Shared Domain (User, BaseEntity)
- AI Module → 独立（调用外部 API）
- Talent Module → Demo 阶段不实现

---

## 5. Demo 范围控制

### 5.1 已实现功能

- 用户认证（登录、登出、Token 刷新）
- 课程管理（CRUD、分类、章节）
- 学习记录（进度追踪、完成状态）
- 积分系统（积分规则、记录、排行榜）
- AI 问答（基础问答）

### 5.2 暂不实现

- 实时评估（WebRTC 复杂度高）
- 九宫格人才地图（功能复杂）
- 复杂报表（Demo 不需要）

---

## 6. 技术决策记录

| 决策 | 选择 | 理由 |
|------|------|------|
| 前端框架 | Vue 3 | 国内文档多，团队熟悉 |
| 后端框架 | Spring Boot 3.x | 企业级标准 |
| 架构模式 | Modular Monolith | Demo 简单，预留扩展性 |
| 数据库 | PostgreSQL | ACID 支持，JSON 类型 |
| 缓存 | Redis | 会话、缓存 |
| 存储方案 | MinIO | S3 兼容，自托管 |

---

*文档创建者: R01 架构师*
*最后更新: 2026-03-02*
