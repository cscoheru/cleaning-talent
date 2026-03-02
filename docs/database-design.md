# 数据库设计文档

## 项目信息

| 字段 | 值 |
|------|-----|
| **文档版本** | v1.0 |
| **创建日期** | 2026-03-02 |
| **创建角色** | R01 架构师 |
| **数据库** | PostgreSQL 15+ |

---

## 1. ER 图

```
┌─────────────┐         ┌─────────────┐         ┌──────────────┐
│    users    │         │   courses   │         │  categories  │
├─────────────┤         ├─────────────┤         ├──────────────┤
│ id (PK)     │         │ id (PK)     │         │ id (PK)      │
│ email (UK)  │         │ title       │         │ name         │
│ password    │    ┌────│ category_id │◄────┐   │ description  │
│ name        │    │    │ description │     │   │ sort_order   │
│ avatar_url  │    │    │ cover_url   │     │   └──────────────┘
│ points      │    │    │ status     │     │
│ role        │    │    │ created_at │     │
│ status      │    │    │ updated_at │     │
│ created_at  │    │    └─────────────┘     │
│ updated_at  │    │                         │
└─────────────┘    │                         │
       │           │                         │
       │           │                         │
       ▼           ▼                         │
┌─────────────────────────────┐              │
│  user_learning_records      │              │
├─────────────────────────────┤              │
│ id (PK)                     │              │
│ user_id (FK → users.id)     │              │
│ course_id (FK → courses.id) │              │
│ enrollment_date             │              │
│ completed_lessons           │              │
│ total_lessons               │              │
│ progress_percentage         │              │
│ status                      │              │
│ completed_at                │              │
│ last_access_at              │              │
│ created_at                  │              │
│ updated_at                  │              │
└─────────────────────────────┘              │
                                                 │
       ┌─────────────┐                          │
       │  chapters   │                          │
├─────────────┤                          │
│ id (PK)     │                          │
│ course_id   │◄─────────────────────────┘
│ title       │
│ description │
│ video_url   │
│ duration    │
│ order_index │
│ created_at  │
└─────────────┘

┌─────────────┐
│points_records│
├─────────────┤
│ id (PK)     │
│ user_id (FK)│
│ points      │
│ type        │
│ description │
│ related_id  │
│ created_at  │
└─────────────┘
```

---

## 2. 表结构定义

### 2.1 users (用户表)

| Column | Type | Nullable | Default | Description |
|--------|------|----------|---------|-------------|
| id | BIGINT | NO | AUTO_INCREMENT | 主键 |
| email | VARCHAR(255) | NO | - | 邮箱（唯一） |
| password | VARCHAR(255) | NO | - | 密码（BCrypt） |
| name | VARCHAR(100) | NO | - | 姓名 |
| avatar_url | VARCHAR(500) | YES | NULL | 头像URL |
| points | INT | NO | 0 | 积分 |
| role | VARCHAR(20) | NO | 'USER' | 角色：USER/ADMIN |
| status | VARCHAR(20) | NO | 'ACTIVE' | 状态：ACTIVE/INACTIVE |
| created_at | TIMESTAMP | NO | NOW() | 创建时间 |
| updated_at | TIMESTAMP | NO | NOW() | 更新时间 |

**Constraints:**
- `uk_users_email`: UNIQUE (email)

**Indexes:**
- `idx_users_email` ON (email)
- `idx_users_status` ON (status)

### 2.2 courses (课程表)

| Column | Type | Nullable | Default | Description |
|--------|------|----------|---------|-------------|
| id | BIGINT | NO | AUTO_INCREMENT | 主键 |
| title | VARCHAR(255) | NO | - | 课程标题 |
| category_id | BIGINT | YES | NULL | 分类ID |
| description | TEXT | YES | NULL | 课程描述 |
| cover_url | VARCHAR(500) | YES | NULL | 封面图URL |
| duration | INT | NO | 0 | 时长（分钟） |
| difficulty | VARCHAR(20) | NO | 'BEGINNER' | 难度：BEGINNER/INTERMEDIATE/ADVANCED |
| points_reward | INT | NO | 100 | 完成奖励积分 |
| status | VARCHAR(20) | NO | 'DRAFT' | 状态：DRAFT/PUBLISHED/ARCHIVED |
| created_by | BIGINT | YES | NULL | 创建者ID |
| created_at | TIMESTAMP | NO | NOW() | 创建时间 |
| updated_at | TIMESTAMP | NO | NOW() | 更新时间 |

**Indexes:**
- `idx_courses_category` ON (category_id)
- `idx_courses_status` ON (status)
- `idx_courses_difficulty` ON (difficulty)

### 2.3 categories (课程分类表)

| Column | Type | Nullable | Default | Description |
|--------|------|----------|---------|-------------|
| id | BIGINT | NO | AUTO_INCREMENT | 主键 |
| name | VARCHAR(100) | NO | - | 分类名称 |
| description | VARCHAR(500) | YES | NULL | 分类描述 |
| sort_order | INT | NO | 0 | 排序 |
| created_at | TIMESTAMP | NO | NOW() | 创建时间 |

### 2.4 user_learning_records (学习记录表)

| Column | Type | Nullable | Default | Description |
|--------|------|----------|---------|-------------|
| id | BIGINT | NO | AUTO_INCREMENT | 主键 |
| user_id | BIGINT | NO | - | 用户ID |
| course_id | BIGINT | NO | - | 课程ID |
| enrollment_date | TIMESTAMP | NO | NOW() | 报名时间 |
| completed_lessons | INT | NO | 0 | 已完成课时 |
| total_lessons | INT | NO | 0 | 总课时数 |
| progress_percentage | DECIMAL(5,2) | NO | 0.00 | 进度百分比 |
| status | VARCHAR(20) | NO | 'IN_PROGRESS' | 状态：NOT_STARTED/IN_PROGRESS/COMPLETED |
| completed_at | TIMESTAMP | YES | NULL | 完成时间 |
| last_access_at | TIMESTAMP | YES | NULL | 最后访问时间 |
| created_at | TIMESTAMP | NO | NOW() | 创建时间 |
| updated_at | TIMESTAMP | NO | NOW() | 更新时间 |

**Constraints:**
- `uk_ulr_user_course`: UNIQUE (user_id, course_id)

**Indexes:**
- `idx_ulr_user_course` ON (user_id, course_id) UNIQUE
- `idx_ulr_user` ON (user_id)
- `idx_ulr_course` ON (course_id)
- `idx_ulr_status` ON (status)

### 2.5 chapters (课程章节表)

| Column | Type | Nullable | Default | Description |
|--------|------|----------|---------|-------------|
| id | BIGINT | NO | AUTO_INCREMENT | 主键 |
| course_id | BIGINT | NO | - | 课程ID |
| title | VARCHAR(255) | NO | - | 章节标题 |
| description | TEXT | YES | NULL | 章节描述 |
| video_url | VARCHAR(500) | YES | NULL | 视频URL |
| duration | INT | NO | 0 | 时长（秒） |
| order_index | INT | NO | 0 | 排序 |
| created_at | TIMESTAMP | NO | NOW() | 创建时间 |

**Indexes:**
- `idx_chapters_course` ON (course_id)
- `idx_chapters_order` ON (course_id, order_index)

### 2.6 points_records (积分记录表)

| Column | Type | Nullable | Default | Description |
|--------|------|----------|---------|-------------|
| id | BIGINT | NO | AUTO_INCREMENT | 主键 |
| user_id | BIGINT | NO | - | 用户ID |
| points | INT | NO | 0 | 积分变动（正/负） |
| type | VARCHAR(50) | NO | - | 类型：COURSE_COMPLETE/DAILY_LOGIN/QUIZ_PASS |
| description | VARCHAR(500) | YES | NULL | 描述 |
| related_id | BIGINT | YES | NULL | 关联ID（课程ID等） |
| created_at | TIMESTAMP | NO | NOW() | 创建时间 |

**Indexes:**
- `idx_pr_user` ON (user_id)
- `idx_pr_type` ON (type)
- `idx_pr_created` ON (created_at DESC)

---

## 3. 逻辑外键关系

Demo 阶段不使用物理外键约束，但记录逻辑关系：

| 表 | 列 | 引用表 | 引用列 |
|----|----|--------|--------|
| courses | category_id | categories | id |
| courses | created_by | users | id |
| user_learning_records | user_id | users | id |
| user_learning_records | course_id | courses | id |
| chapters | course_id | courses | id |
| points_records | user_id | users | id |

---

## 4. 数据库约束规则

### 4.1 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 主键 | `id` | `id` (BIGINT) |
| 外键 | `{table}_id` | `user_id`, `course_id` |
| 时间戳 | `{action}_at` | `created_at`, `updated_at` |
| 状态字段 | `status` | `status` (VARCHAR) |
| 布尔字段 | `is_{attribute}` | `is_active` |

### 4.2 字段类型规范

| 用途 | 类型 | 说明 |
|------|------|------|
| 主键/外键 | BIGINT | 8 字节整数 |
| 短文本 | VARCHAR(255) | 标题、名称 |
| 中等文本 | VARCHAR(500) | URL、描述 |
| 长文本 | TEXT | 详细描述 |
| 金额/积分 | INT | 正整数 |
| 百分比 | DECIMAL(5,2) | 0.00 - 100.00 |
| 时间 | TIMESTAMP | 时间戳 |

### 4.3 索引规范

- 所有外键列必须有索引
- 频繁查询字段添加索引
- 唯一约束使用 UNIQUE 索引
- 组合查询使用组合索引

### 4.4 唯一约束

```sql
-- 用户邮箱唯一
ALTER TABLE users ADD CONSTRAINT uk_users_email UNIQUE (email);

-- 用户-课程学习记录唯一
ALTER TABLE user_learning_records ADD CONSTRAINT uk_ulr_user_course UNIQUE (user_id, course_id);
```

---

## 5. 初始数据

### 5.1 分类数据

```sql
INSERT INTO categories (name, description, sort_order) VALUES
('技术', '技术类课程', 1),
('管理', '管理类课程', 2),
('职场', '职场技能课程', 3);
```

### 5.2 初始用户

```sql
-- 密码: password123 (BCrypt 加密后的示例)
INSERT INTO users (email, password, name, role, points) VALUES
('admin@lms.demo', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '管理员', 'ADMIN', 0),
('user@lms.demo', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '测试用户', 'USER', 100);
```

### 5.3 示例课程

```sql
INSERT INTO courses (title, category_id, description, difficulty, points_reward, status, created_by) VALUES
('Vue 3 快速入门', 1, '从零开始学习 Vue 3 框架', 'BEGINNER', 100, 'PUBLISHED', 1),
('Spring Boot 实战', 1, '企业级 Spring Boot 开发实战', 'INTERMEDIATE', 200, 'PUBLISHED', 1),
('团队管理基础', 2, '新晋管理者必备技能', 'BEGINNER', 150, 'PUBLISHED', 1);
```

---

## 6. 积分规则

| 事件 | 积分 | 类型 |
|------|------|------|
| 完成一门课程 | 课程设定值 | COURSE_COMPLETE |
| 每日登录 | +5 | DAILY_LOGIN |
| 通过测验 | +20 | QUIZ_PASS |

---

*文档创建者: R01 架构师*
*最后更新: 2026-03-02*
