-- cleaning_talent_demo 测试数据
-- 创建时间: 2026-03-02
-- 创建者: R02 数据库工程师

-- 切换到目标数据库
\c cleaning_talent_demo

-- ============================================
-- 1. 分类数据 (categories)
-- ============================================
INSERT INTO categories (name, description, sort_order) VALUES
('技术', '技术类课程，包含编程开发、系统运维等内容', 1),
('管理', '管理类课程，包含团队管理、项目管理等内容', 2),
('职场', '职场技能课程，包含沟通协作、职业规划等内容', 3)
ON CONFLICT DO NOTHING;

-- ============================================
-- 2. 初始用户 (users)
-- ============================================
-- 密码: password123 (BCrypt 加密)
-- admin@lms.demo / password123
-- user@lms.demo / password123
INSERT INTO users (email, password, name, avatar_url, points, role, status) VALUES
('admin@lms.demo', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '系统管理员', NULL, 0, 'ADMIN', 'ACTIVE'),
('user@lms.demo', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '测试用户', NULL, 100, 'USER', 'ACTIVE'),
('zhang.san@lms.demo', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '张三', NULL, 50, 'USER', 'ACTIVE'),
('li.si@lms.demo', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '李四', NULL, 200, 'USER', 'ACTIVE'),
('wang.wu@lms.demo', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '王五', NULL, 150, 'USER', 'ACTIVE')
ON CONFLICT (email) DO NOTHING;

-- ============================================
-- 3. 示例课程 (courses)
-- ============================================
INSERT INTO courses (title, category_id, description, cover_url, duration, difficulty, points_reward, status, created_by) VALUES
-- 技术类课程
('Vue 3 快速入门', 1, '从零开始学习 Vue 3 框架，掌握组合式 API、响应式原理等核心概念', 'https://placehold.co/400x300/42b883/white?text=Vue3', 180, 'BEGINNER', 100, 'PUBLISHED', 1),
('Spring Boot 实战', 1, '企业级 Spring Boot 开发实战，包含 REST API、数据库操作、安全认证等', 'https://placehold.co/400x300/6db33f/white?text=SpringBoot', 240, 'INTERMEDIATE', 200, 'PUBLISHED', 1),
('TypeScript 进阶', 1, '深入理解 TypeScript 高级类型、泛型、装饰器等特性', 'https://placehold.co/400x300/3178c6/white?text=TypeScript', 150, 'INTERMEDIATE', 150, 'PUBLISHED', 1),
('Docker 容器化部署', 1, '学习 Docker 容器技术，掌握镜像构建、容器编排等技能', 'https://placehold.co/400x300/2496ed/white?text=Docker', 120, 'BEGINNER', 100, 'PUBLISHED', 1),

-- 管理类课程
('团队管理基础', 2, '新晋管理者必备技能，包含团队建设、绩效管理、冲突解决等', 'https://placehold.co/400x300/f59e0b/white?text=TeamMgmt', 90, 'BEGINNER', 150, 'PUBLISHED', 1),
('敏捷项目管理', 2, '掌握 Scrum 敏捷开发流程，提升团队协作效率', 'https://placehold.co/400x300/6366f1/white?text=Agile', 120, 'INTERMEDIATE', 180, 'PUBLISHED', 1),
('高效沟通技巧', 2, '提升职场沟通能力，学会向上汇报、跨部门协作等实用技能', 'https://placehold.co/400x300/10b981/white?text=Communication', 60, 'BEGINNER', 100, 'PUBLISHED', 1),

-- 职场类课程
('职业规划入门', 3, '帮助你理清职业发展路径，制定个人成长计划', 'https://placehold.co/400x300/8b5cf6/white?text=Career', 45, 'BEGINNER', 80, 'PUBLISHED', 1),
('时间管理秘籍', 3, '学习高效时间管理方法，提升工作效率', 'https://placehold.co/400x300/ec4899/white?text=TimeMgmt', 30, 'BEGINNER', 50, 'PUBLISHED', 1),
('演讲与表达', 3, '提升公众演讲能力，让你的表达更有说服力', 'https://placehold.co/400x300/eab308/white?text=Presentation', 75, 'INTERMEDIATE', 120, 'PUBLISHED', 1)
ON CONFLICT DO NOTHING;

-- ============================================
-- 4. 课程章节 (chapters)
-- ============================================
-- Vue 3 快速入门章节
INSERT INTO chapters (course_id, title, description, video_url, duration, order_index) VALUES
(1, '第1章：Vue 3 简介', 'Vue 3 框架介绍与开发环境搭建', 'https://example.com/video1.mp4', 600, 1),
(1, '第2章：组合式 API 基础', 'setup 函数、ref、reactive 等核心概念', 'https://example.com/video2.mp4', 900, 2),
(1, '第3章：响应式原理', '深入理解 Vue 3 的响应式系统', 'https://example.com/video3.mp4', 1200, 3),
(1, '第4章：组件通信', '父子组件、兄弟组件间的数据传递', 'https://example.com/video4.mp4', 1080, 4)
ON CONFLICT DO NOTHING;

-- Spring Boot 实战章节
INSERT INTO chapters (course_id, title, description, video_url, duration, order_index) VALUES
(2, '第1章：Spring Boot 快速开始', '项目初始化与第一个 REST API', 'https://example.com/video5.mp4', 720, 1),
(2, '第2章：数据库操作', 'Spring Data JPA 与 MyBatis 集成', 'https://example.com/video6.mp4', 1440, 2),
(2, '第3章：Spring Security', '认证与授权实现', 'https://example.com/video7.mp4', 1800, 3),
(2, '第4章：接口测试与部署', '单元测试、集成测试与生产部署', 'https://example.com/video8.mp4', 900, 4)
ON CONFLICT DO NOTHING;

-- 团队管理基础章节
INSERT INTO chapters (course_id, title, description, video_url, duration, order_index) VALUES
(5, '第1章：管理者角色认知', '从执行者到管理者的思维转变', 'https://example.com/video9.mp4', 540, 1),
(5, '第2章：团队建设', '如何打造高绩效团队', 'https://example.com/video10.mp4', 720, 2),
(5, '第3章：绩效管理', '目标设定与绩效评估技巧', 'https://example.com/video11.mp4', 900, 3)
ON CONFLICT DO NOTHING;

-- ============================================
-- 5. 学习记录 (user_learning_records)
-- ============================================
-- 用户 2 (user@lms.demo) 正在学习 Vue 3 课程，完成 50%
INSERT INTO user_learning_records (user_id, course_id, enrollment_date, completed_lessons, total_lessons, progress_percentage, status, last_access_at) VALUES
(2, 1, CURRENT_TIMESTAMP - INTERVAL '7 days', 2, 4, 50.00, 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '1 day')
ON CONFLICT (user_id, course_id) DO NOTHING;

-- 用户 3 (zhang.san) 已完成团队管理课程
INSERT INTO user_learning_records (user_id, course_id, enrollment_date, completed_lessons, total_lessons, progress_percentage, status, completed_at, last_access_at) VALUES
(3, 5, CURRENT_TIMESTAMP - INTERVAL '14 days', 3, 3, 100.00, 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day')
ON CONFLICT (user_id, course_id) DO NOTHING;

-- 用户 4 (li.si) 正在学习 Spring Boot，完成 25%
INSERT INTO user_learning_records (user_id, course_id, enrollment_date, completed_lessons, total_lessons, progress_percentage, status, last_access_at) VALUES
(4, 2, CURRENT_TIMESTAMP - INTERVAL '3 days', 1, 4, 25.00, 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '2 hours')
ON CONFLICT (user_id, course_id) DO NOTHING;

-- ============================================
-- 6. 积分记录 (points_records)
-- ============================================
-- 用户 3 完成课程获得的积分
INSERT INTO points_records (user_id, points, type, description, related_id) VALUES
(3, 150, 'COURSE_COMPLETE', '完成课程：团队管理基础', 5)
ON CONFLICT DO NOTHING;

-- 用户 4 每日登录积分
INSERT INTO points_records (user_id, points, type, description, related_id) VALUES
(4, 5, 'DAILY_LOGIN', '每日登录奖励', NULL),
(4, 5, 'DAILY_LOGIN', '每日登录奖励', NULL),
(4, 5, 'DAILY_LOGIN', '每日登录奖励', NULL)
ON CONFLICT DO NOTHING;

-- 用户 5 测验通过积分
INSERT INTO points_records (user_id, points, type, description, related_id) VALUES
(5, 20, 'QUIZ_PASS', '通过测验：Vue 3 基础知识', 1)
ON CONFLICT DO NOTHING;
