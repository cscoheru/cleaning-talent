-- cleaning_talent_demo 数据库表结构
-- 创建时间: 2026-03-02
-- 创建者: R02 数据库工程师
-- 数据库: PostgreSQL 15+

-- 切换到目标数据库
\c cleaning_talent_demo

-- 创建扩展
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ============================================
-- 1. users (用户表)
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    avatar_url VARCHAR(500),
    points INT DEFAULT 0,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_users_email UNIQUE (email)
);

-- 索引
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_status ON users(status);

-- ============================================
-- 2. categories (课程分类表)
-- ============================================
CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    sort_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 3. courses (课程表)
-- ============================================
CREATE TABLE IF NOT EXISTS courses (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    category_id BIGINT,
    description TEXT,
    cover_url VARCHAR(500),
    duration INT DEFAULT 0,
    difficulty VARCHAR(20) NOT NULL DEFAULT 'BEGINNER',
    points_reward INT DEFAULT 100,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_courses_difficulty CHECK (difficulty IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED')),
    CONSTRAINT chk_courses_status CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED'))
);

-- 索引
CREATE INDEX IF NOT EXISTS idx_courses_category ON courses(category_id);
CREATE INDEX IF NOT EXISTS idx_courses_status ON courses(status);
CREATE INDEX IF NOT EXISTS idx_courses_difficulty ON courses(difficulty);

-- ============================================
-- 4. chapters (课程章节表)
-- ============================================
CREATE TABLE IF NOT EXISTS chapters (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    video_url VARCHAR(500),
    duration INT DEFAULT 0,
    order_index INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 索引
CREATE INDEX IF NOT EXISTS idx_chapters_course ON chapters(course_id);
CREATE INDEX IF NOT EXISTS idx_chapters_order ON chapters(course_id, order_index);

-- ============================================
-- 5. user_learning_records (学习记录表)
-- ============================================
CREATE TABLE IF NOT EXISTS user_learning_records (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_lessons INT DEFAULT 0,
    total_lessons INT DEFAULT 0,
    progress_percentage DECIMAL(5,2) DEFAULT 0.00,
    status VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS',
    completed_at TIMESTAMP,
    last_access_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_ulr_user_course UNIQUE (user_id, course_id),
    CONSTRAINT chk_ulr_status CHECK (status IN ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED')),
    CONSTRAINT chk_ulr_progress CHECK (progress_percentage >= 0 AND progress_percentage <= 100)
);

-- 索引
CREATE INDEX IF NOT EXISTS idx_ulr_user_course ON user_learning_records(user_id, course_id);
CREATE INDEX IF NOT EXISTS idx_ulr_user ON user_learning_records(user_id);
CREATE INDEX IF NOT EXISTS idx_ulr_course ON user_learning_records(course_id);
CREATE INDEX IF NOT EXISTS idx_ulr_status ON user_learning_records(status);

-- ============================================
-- 6. points_records (积分记录表)
-- ============================================
CREATE TABLE IF NOT EXISTS points_records (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    points INT DEFAULT 0,
    type VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    related_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 索引
CREATE INDEX IF NOT EXISTS idx_pr_user ON points_records(user_id);
CREATE INDEX IF NOT EXISTS idx_pr_type ON points_records(type);
CREATE INDEX IF NOT EXISTS idx_pr_created ON points_records(created_at DESC);

-- ============================================
-- 自动更新 updated_at 触发器
-- ============================================
-- 创建通用的更新时间戳函数
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 为 users 表创建触发器
DROP TRIGGER IF EXISTS update_users_updated_at ON users;
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- 为 courses 表创建触发器
DROP TRIGGER IF EXISTS update_courses_updated_at ON courses;
CREATE TRIGGER update_courses_updated_at
    BEFORE UPDATE ON courses
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- 为 user_learning_records 表创建触发器
DROP TRIGGER IF EXISTS update_ulr_updated_at ON user_learning_records;
CREATE TRIGGER update_ulr_updated_at
    BEFORE UPDATE ON user_learning_records
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
