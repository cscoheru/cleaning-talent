# API 接口规范文档

## 项目信息

| 字段 | 值 |
|------|-----|
| **文档版本** | v1.0 |
| **创建日期** | 2026-03-02 |
| **创建角色** | R01 架构师 |
| **Base URL** | /api/v1 |

---

## 1. 统一响应格式

### 1.1 成功响应

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1709356800000
}
```

### 1.2 错误响应

```json
{
  "code": 400,
  "message": "参数验证失败",
  "data": null,
  "timestamp": 1709356800000,
  "errors": [
    {
      "field": "email",
      "message": "邮箱格式不正确"
    }
  ]
}
```

### 1.3 分页响应

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

---

## 2. 错误码定义

### 2.1 HTTP 标准错误码

| Code | HTTP Status | Message | Description |
|------|-------------|---------|-------------|
| 200 | 200 | success | 请求成功 |
| 400 | 400 | bad_request | 请求参数错误 |
| 401 | 401 | unauthorized | 未认证 |
| 403 | 403 | forbidden | 无权限 |
| 404 | 404 | not_found | 资源不存在 |
| 409 | 409 | conflict | 资源冲突 |
| 429 | 429 | too_many_requests | 请求过于频繁 |
| 500 | 500 | internal_error | 服务器错误 |
| 503 | 503 | service_unavailable | 服务不可用 |

### 2.2 业务错误码

| Code | Message | Description |
|------|---------|-------------|
| **用户相关 (1001-1099)** |
| 1001 | user_not_found | 用户不存在 |
| 1002 | invalid_credentials | 用户名或密码错误 |
| 1003 | token_expired | Token 已过期 |
| 1004 | token_invalid | Token 无效 |
| 1005 | user_already_exists | 用户已存在 |
| **课程相关 (2001-2099)** |
| 2001 | course_not_found | 课程不存在 |
| 2002 | course_already_enrolled | 已报名该课程 |
| 2003 | chapter_not_found | 章节不存在 |
| **学习相关 (3001-3099)** |
| 3001 | lesson_not_completed | 课程未完成 |
| 3002 | learning_record_not_found | 学习记录不存在 |
| **积分相关 (4001-4099)** |
| 4001 | insufficient_points | 积分不足 |

---

## 3. 认证方式

### 3.1 Header Token

```
Authorization: Bearer {jwt_token}
```

### 3.2 Cookie Token（Demo 阶段可选）

```
access_token={jwt_token}; HttpOnly; Secure; SameSite=Strict
```

---

## 4. 核心接口列表

### 4.1 认证模块 (Auth)

#### POST /api/v1/auth/login

用户登录

**Request:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response (200):**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "email": "user@example.com",
      "name": "张三",
      "avatarUrl": "https://...",
      "points": 100,
      "role": "USER"
    }
  },
  "timestamp": 1709356800000
}
```

**Response (1002):**
```json
{
  "code": 1002,
  "message": "invalid_credentials",
  "data": null,
  "timestamp": 1709356800000
}
```

#### POST /api/v1/auth/logout

用户登出

**Request:**
```json
{}
```

**Response (200):**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "message": "登出成功"
  },
  "timestamp": 1709356800000
}
```

#### POST /api/v1/auth/refresh

刷新 Token

**Request:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response (200):**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  },
  "timestamp": 1709356800000
}
```

#### GET /api/v1/auth/me

获取当前用户信息

**Response (200):**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "name": "张三",
    "avatarUrl": "https://...",
    "points": 100,
    "role": "USER"
  },
  "timestamp": 1709356800000
}
```

---

### 4.2 课程模块 (Course)

#### GET /api/v1/courses

获取课程列表

**Query Parameters:**
- `page` (int, default: 1) - 页码
- `size` (int, default: 20) - 每页数量
- `category` (long, optional) - 分类ID
- `difficulty` (string, optional) - 难度：BEGINNER/INTERMEDIATE/ADVANCED
- `status` (string, optional) - 状态：DRAFT/PUBLISHED/ARCHIVED

**Response (200):**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "items": [
      {
        "id": 1,
        "title": "Vue 3 快速入门",
        "description": "从零开始学习 Vue 3",
        "coverUrl": "https://...",
        "duration": 120,
        "difficulty": "BEGINNER",
        "pointsReward": 100,
        "category": {
          "id": 1,
          "name": "技术"
        },
        "status": "PUBLISHED",
        "createdAt": "2026-03-01T00:00:00Z"
      }
    ],
    "total": 100,
    "page": 1,
    "pageSize": 20,
    "totalPages": 5
  },
  "timestamp": 1709356800000
}
```

#### GET /api/v1/courses/{id}

获取课程详情

**Response (200):**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "Vue 3 快速入门",
    "description": "从零开始学习 Vue 3",
    "coverUrl": "https://...",
    "duration": 120,
    "difficulty": "BEGINNER",
    "pointsReward": 100,
    "category": {
      "id": 1,
      "name": "技术"
    },
    "status": "PUBLISHED",
    "createdAt": "2026-03-01T00:00:00Z",
    "chapters": [
      {
        "id": 1,
        "title": "第一章：Vue 简介",
        "description": "Vue 3 框架介绍",
        "videoUrl": "https://...",
        "duration": 600,
        "orderIndex": 1
      }
    ]
  },
  "timestamp": 1709356800000
}
```

#### POST /api/v1/courses

创建课程（需要 ADMIN 权限）

**Request:**
```json
{
  "title": "Vue 3 快速入门",
  "categoryId": 1,
  "description": "从零开始学习 Vue 3",
  "coverUrl": "https://...",
  "duration": 120,
  "difficulty": "BEGINNER",
  "pointsReward": 100,
  "status": "PUBLISHED"
}
```

**Response (200):**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "Vue 3 快速入门",
    ...
  },
  "timestamp": 1709356800000
}
```

#### PUT /api/v1/courses/{id}

更新课程（需要 ADMIN 权限）

**Request:**
```json
{
  "title": "Vue 3 快速入门（更新版）",
  "categoryId": 1,
  "description": "从零开始学习 Vue 3",
  ...
}
```

#### DELETE /api/v1/courses/{id}

删除课程（需要 ADMIN 权限）

**Response (200):**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "message": "课程删除成功"
  },
  "timestamp": 1709356800000
}
```

---

### 4.3 学习模块 (Learning)

#### GET /api/v1/learning/progress

获取学习进度

**Query Parameters:**
- `courseId` (long, optional) - 课程ID

**Response (200):**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "courseId": 1,
    "completedLessons": 5,
    "totalLessons": 10,
    "progressPercentage": 50.00,
    "status": "IN_PROGRESS",
    "enrollmentDate": "2026-03-01T00:00:00Z",
    "lastAccessAt": "2026-03-02T10:00:00Z"
  },
  "timestamp": 1709356800000
}
```

#### POST /api/v1/learning/record

记录学习

**Request:**
```json
{
  "lessonId": 1,
  "duration": 300
}
```

**Response (200):**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "userId": 1,
    "lessonId": 1,
    "duration": 300,
    "createdAt": "2026-03-02T10:00:00Z"
  },
  "timestamp": 1709356800000
}
```

#### POST /api/v1/learning/complete

完成课程

**Request:**
```json
{
  "courseId": 1
}
```

**Response (200):**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "message": "课程完成，获得 100 积分",
    "pointsEarned": 100
  },
  "timestamp": 1709356800000
}
```

#### GET /api/v1/learning/history

获取学习历史

**Query Parameters:**
- `page` (int, default: 1)
- `size` (int, default: 20)

**Response (200):**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "items": [
      {
        "courseId": 1,
        "courseTitle": "Vue 3 快速入门",
        "progressPercentage": 100.00,
        "status": "COMPLETED",
        "completedAt": "2026-03-02T10:00:00Z"
      }
    ],
    "total": 10,
    "page": 1,
    "pageSize": 20
  },
  "timestamp": 1709356800000
}
```

---

### 4.4 积分模块 (Points)

#### GET /api/v1/points/balance

获取积分余额

**Response (200):**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "balance": 1000,
    "rank": 5
  },
  "timestamp": 1709356800000
}
```

#### GET /api/v1/points/history

获取积分历史

**Query Parameters:**
- `page` (int, default: 1)
- `size` (int, default: 20)
- `type` (string, optional) - 积分类型

**Response (200):**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "items": [
      {
        "id": 1,
        "points": 100,
        "type": "COURSE_COMPLETE",
        "description": "完成《Vue 3 快速入门》课程",
        "createdAt": "2026-03-02T10:00:00Z"
      }
    ],
    "total": 50,
    "page": 1,
    "pageSize": 20
  },
  "timestamp": 1709356800000
}
```

#### GET /api/v1/points/ranking

获取积分排行榜

**Query Parameters:**
- `page` (int, default: 1)
- `size` (int, default: 20)

**Response (200):**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "items": [
      {
        "rank": 1,
        "userId": 1,
        "userName": "张三",
        "avatarUrl": "https://...",
        "points": 2000
      }
    ],
    "total": 100,
    "page": 1,
    "pageSize": 20
  },
  "timestamp": 1709356800000
}
```

---

### 4.5 AI 模块 (AI)

#### POST /api/v1/ai/chat

AI 问答

**Request:**
```json
{
  "question": "Vue 3 和 Vue 2 的主要区别是什么？",
  "context": {
    "courseId": 1,
    "lessonId": 1
  }
}
```

**Response (200):**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "answer": "Vue 3 相比 Vue 2 有以下主要改进：...",
    "sources": [
      {
        "title": "Vue 3 官方文档",
        "url": "https://..."
      }
    ]
  },
  "timestamp": 1709356800000
}
```

#### GET /api/v1/ai/recommendations

学习推荐

**Query Parameters:**
- `userId` (long) - 用户ID

**Response (200):**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "courses": [
      {
        "id": 2,
        "title": "Spring Boot 实战",
        "reason": "基于您已完成的《Vue 3 快速入门》课程",
        "matchScore": 85
      }
    ]
  },
  "timestamp": 1709356800000
}
```

---

## 5. 分页参数规范

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| page | int | 1 | 页码（从 1 开始） |
| size | int | 20 | 每页数量（最大 100） |
| sort | string | - | 排序字段 |

---

*文档创建者: R01 架构师*
*最后更新: 2026-03-02*
