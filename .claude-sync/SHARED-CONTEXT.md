# 共享上下文

## 项目信息

- **项目名称**: cleaning_Talent (企业学习与人才管理一体化平台)
- **阶段**: Demo/POC
- **开发模式**: 单人 + Claude Code 协作
- **工作目录**: `/Users/kjonekong/Documents/个人/cleaning_Talent`
- **GitHub**: (待创建)

## 技术栈（R01 已确认 ✅）

| 层级 | 技术选择 | 版本 | 说明 |
|------|---------|------|------|
| 前端 | Vue 3 + TypeScript | 3.4+ | 渐进式框架，类型安全 |
| 前端构建 | Vite | 5.x | 快速 HMR，优化构建 |
| 前端UI | Element Plus | Latest | 企业级组件库 |
| 前端状态 | Pinia | Latest | Vue 3 官方状态管理 |
| 后端 | Spring Boot | 3.2+ | 企业级 Java 框架 |
| 后端语言 | Java | 17 | LTS 版本 |
| 后端ORM | MyBatis-Plus | 3.5+ | 简化 CRUD |
| 后端安全 | Spring Security + JWT | 6.x | 认证授权 |
| AI | FastAPI | 0.100+ | Python AI 生态 |
| AI框架 | LangChain | Latest | LLM 编排 |
| 数据库 | PostgreSQL | 15+ | ACID, JSON 支持 |
| 缓存 | Redis | 7.x | 会话、缓存 |
| 存储 | MinIO | Latest | S3 兼容存储 |

## API 规范（R01 已确认 ✅）

### 统一响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1709356800000
}
```

### 错误响应格式
```json
{
  "code": 400,
  "message": "bad_request",
  "data": null,
  "timestamp": 1709356800000,
  "errors": []
}
```

### 分页响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "items": [],
    "total": 100,
    "page": 1,
    "pageSize": 20,
    "totalPages": 5
  },
  "timestamp": 1709356800000
}
```

### 错误码范围
- 200/400/401/403/404/409/429/500/503: HTTP 标准错误
- 1001-1099: 用户相关错误
- 2001-2099: 课程相关错误
- 3001-3099: 学习相关错误
- 4001-4099: 积分相关错误

### 认证方式
```
Authorization: Bearer {jwt_token}
```

### 分页参数
```
?page=1&size=20&sort=createdAt
```

## 模块间通信规则（R01 已定义 ✅）

### 接口依赖原则
```java
// ✅ 正确：依赖接口
@Autowired
private CourseService courseService;  // Interface

// ❌ 错误：跨模块直接访问 Repository
@Autowired
private CourseRepository courseRepository;  // 禁止！
```

### DTO 传输原则
```java
// ✅ 正确：使用 DTO 跨模块传输数据
CourseDTO course = courseService.getCourseById(courseId);
```

### 模块依赖关系
- 所有业务模块 → Shared Domain (User, BaseEntity)
- AI Module → 独立（调用外部 API）
- Talent Module → Demo 阶段不实现

### 模块列表
- **Auth** (认证): 用户认证、授权、Token 管理
- **Course** (课程): 课程 CRUD、分类管理、章节管理
- **Learning** (学习): 学习记录、进度追踪、完成状态
- **Points** (积分): 积分规则、积分记录、排行榜
- **AI** (AI): AI 问答、学习推荐
- **Talent** (人才): 人才评价、九宫格 (Demo 暂不实现)

## 架构决策（R01 已确认 ✅）

### 架构模式
**Modular Monolith (模块化单体)**

- Demo 阶段使用模块化单体架构
- 模块间通过接口通信，禁止跨模块直接访问实体层
- 为未来微服务化预留接口边界

### Demo 功能范围

#### 已实现
- 用户认证（登录、登出、Token 刷新）
- 课程管理（CRUD、分类、章节）
- 学习记录（进度追踪、完成状态）
- 积分系统（积分规则、记录、排行榜）
- AI 问答（基础问答）

#### 暂不实现
- 实时评估（WebRTC 复杂度高）
- 九宫格人才地图（功能复杂）
- 复杂报表（Demo 不需要）

## 数据库连接（待 R02 配置）

```bash
# 节点 A PostgreSQL
Host: 139.224.42.111
Port: 5432
Database: cleaning_talent_demo
User: postgres
Password: WhjQTPAwInc5Vav3sDWe

# 本地开发（可选）
Host: localhost
Port: 5432
```

## Redis 连接

```bash
# 节点 A Redis
Host: 139.224.42.111
Port: 6379
Password: FWD4D75OKyQS7HOluA6J
```

## MinIO 连接

```bash
# 节点 A MinIO
Endpoint: 139.224.42.111:9000
Access Key: admin
Secret Key: xhOSMeHNmxCgNTBpoQfH
Bucket: cleaning-talent-demo
```

## 部署信息（R01 已规划 ✅）

### 生产环境
```
前端: Vercel (https://lms-demo.vercel.app)
后端: Railway (https://lms-backend.railway.app)
AI 服务: Node A (http://139.224.42.111:8000)
基础设施: Node A (PostgreSQL, Redis, MinIO)
```

### 本地开发
```
前端: localhost:5173 (Vite Dev Server)
后端: localhost:8080 (Spring Boot)
AI 服务: localhost:8000 (FastAPI)
基础设施: Docker Compose (PostgreSQL, Redis, MinIO)
```

### CORS 白名单
```
Backend: https://lms-demo.vercel.app,http://localhost:5173
AI Service: https://lms-demo.vercel.app,https://lms-backend.railway.app,http://localhost:5173,http://localhost:8080
```

## AI 服务信息（R05 已完成 ✅）

### AI 服务 API 端点

| 端点 | 方法 | 说明 |
|------|------|------|
| `/api/v1/qa/ask` | POST | AI 问答 |
| `/api/v1/qa/health` | GET | 问答服务健康检查 |
| `/api/v1/qa/history/{user_id}` | GET | 获取对话历史 |
| `/api/v1/qa/history/{user_id}` | DELETE | 清除对话历史 |
| `/api/v1/health` | GET | 服务健康检查 |
| `/docs` | GET | Swagger API 文档 |

### AI 问答请求格式

```json
POST /api/v1/qa/ask
{
  "question": "什么是学习地图？",
  "context": "可选的上下文信息",
  "user_id": "user_123"
}
```

### AI 问答响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "answer": "AI 回答内容",
    "user_id": "user_123",
    "usage": {
      "prompt_tokens": 100,
      "completion_tokens": 200,
      "total_tokens": 300,
      "model": "qwen-turbo"
    }
  },
  "timestamp": 1709356800000
}
```

### Mock 模式

当未配置通义千问 API Key 时，AI 服务将使用 Mock 模式返回预设的模拟回答，方便开发测试。

## 重要约定

### 命名规范

- Java 类名：`PascalCase`，如 `UserController`
- Java 方法名：`camelCase`，如 `getUserById`
- 数据库表名：`snake_case`，如 `user_learning_records`
- API 路径：`kebab-case`，如 `/api/v1/user-profile`
- Vue 组件名：`PascalCase`，如 `UserProfile.vue`

### Git 提交规范

```
feat: 新功能
fix: 修复 Bug
docs: 文档
style: 代码格式（不影响功能）
refactor: 重构
test: 测试
chore: 构建/工具

示例:
git commit -m "feat(R03): 实现用户登录接口"
git commit -m "fix(R04): 修复登录页面样式问题"
```

### 分支策略

```
main: 主分支，生产环境
develop: 开发分支
feature/xxx: 功能分支

Demo 阶段简化：
- main: 主分支，直接开发
- 不使用功能分支，减少复杂度
```

## 待决策事项

| 待决策项 | 影响 | 负责角色 | 状态 |
|---------|------|---------|------|
| ~~技术栈选型~~ | 整体架构 | R01 | ✅ 已完成 |
| 向量数据库选型 | AI RAG 功能 | R05 | Demo 暂不实现 |
| ~~Demo 功能范围~~ | 开发范围 | R01 | ✅ 已完成 |
| ~~AI 服务框架~~ | AI 功能 | R05 | ✅ 已完成 |

## 重要提醒

⚠️ **注意**：
- 任何技术决策变更必须先在此文档记录
- 其他角色查看此文档获取最新信息
- 决策有争议时由 R01（架构师）最终决定
- 每天开始工作前，请先查看此文档的最新状态
