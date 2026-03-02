# 📋 任务卡片：R02 数据库工程师

## 角色信息

| 字段 | 值 |
|------|-----|
| **角色编号** | R02 |
| **角色名称** | 数据库工程师 |
| **主要职责** | 数据库设计、SQL 脚本编写、数据初始化 |
| **工作时长** | 2-3 小时 |
| **开始条件** | 等待 R01 完成数据库设计文档 |

---

## 会话目标

根据 R01 提供的数据库设计文档，初始化 Demo 数据库，创建表结构并导入测试数据。

---

## 依赖检查

**开始前请确认**：

```bash
# 检查 R01 是否完成
cat .claude-sync/SESSION-LOG.md | grep "R01"

# 检查数据库设计文档是否存在
ls docs/database-design.md

# 检查依赖关系
cat .claude-sync/DEPENDENCY-TREE.md
```

如果 R01 未完成，请等待或开始只读研究。

---

## 需要读取的文件

```bash
# R01 的数据库设计文档
cat docs/database-design.md

# 共享上下文（数据库连接信息）
cat .claude-sync/SHARED-CONTEXT.md
```

---

## 任务清单

### 第一阶段：连接测试（30分钟）

- [ ] 测试节点 A PostgreSQL 连接
  ```bash
  # 连接测试
  ssh root@139.224.42.111 'docker exec postgres psql -U postgres -c "SELECT version();"'
  ```

- [ ] 创建数据库
  ```sql
  CREATE DATABASE cleaning_talent_demo;
  ```

- [ ] 创建必要扩展
  ```sql
  \c cleaning_talent_demo
  CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
  CREATE EXTENSION IF NOT EXISTS "pgcrypto";
  ```

### 第二阶段：表结构创建（1小时）

- [ ] 创建用户表 (users)
  ```sql
  CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    department VARCHAR(100),
    position VARCHAR(100),
    points INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );
  ```

- [ ] 创建课程表 (courses)
- [ ] 创建学习记录表 (user_learning_records)
- [ ] 创建学习地图表 (learning_maps)
- [ ] 创建积分记录表 (points_records)
- [ ] 创建 AI 问答日志表 (ai_qa_logs)

- [ ] 创建索引
  ```sql
  CREATE INDEX idx_users_email ON users(email);
  CREATE INDEX idx_learning_records_user ON user_learning_records(user_id);
  ```

### 第三阶段：测试数据（30分钟）

- [ ] 创建测试用户数据
  ```sql
  INSERT INTO users (username, email, password_hash, full_name) VALUES
  ('admin', 'admin@example.com', '...', '系统管理员'),
  ('user1', 'user1@example.com', '...', '张三');
  ```

- [ ] 创建测试课程数据
- [ ] 创建测试学习地图数据

### 第四阶段：后端配置（30分钟）

- [ ] 配置后端数据库连接
  ```yaml
  # backend/src/main/resources/application.yml
  spring:
    datasource:
      url: jdbc:postgresql://139.224.42.111:5432/cleaning_talent_demo
      username: postgres
      password: WhjQTPAwInc5Vav3sDWe
  ```

- [ ] 配置 MyBatis-Plus
- [ ] 测试连接

---

## 输出文件

1. **`backend/src/main/resources/schema.sql`** - 建表 SQL 脚本
2. **`backend/src/main/resources/data.sql`** - 测试数据 SQL 脚本
3. **`backend/src/main/resources/application.yml`** - 数据库配置

---

## 完成后要更新的文件

```bash
# 1. 更新进度看板
# 编辑 .claude-sync/PROGRESS-BOARD.md
# 将 R02 状态改为 ✅ 完成

# 2. 更新共享上下文
# 编辑 .claude-sync/SHARED-CONTEXT.md
# 确认数据库连接信息正确

# 3. 更新依赖树
# 编辑 .claude-sync/DEPENDENCY-TREE.md
# 解锁 R03 后端工程师

# 4. 更新会话日志
# 编辑 .claude-sync/SESSION-LOG.md
# 记录本次会话的产出

# 5. 更新变更日志
# 编辑 .claude-sync/CHANGE-LOG.md
```

---

## 重要提醒

⚠️ **数据库连接信息**（来自节点 A）：
- Host: 139.224.42.111
- Port: 5432
- User: postgres
- Password: WhjQTPAwInc5Vav3sDWe
- Database: cleaning_talent_demo

⚠️ **安全注意事项**：
- 不要在代码中硬编码密码
- 使用环境变量管理敏感信息
- .env 文件不要提交到 Git

---

## SQL 脚本模板

### schema.sql 模板

```sql
-- cleaning_talent_demo 数据库表结构
-- 创建时间: YYYY-MM-DD
-- 创建者: R02 数据库工程师

\c cleaning_talent_demo

-- 创建扩展
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 用户表
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    -- ... 其他字段
);

-- 更多表...
```

### data.sql 模板

```sql
-- cleaning_talent_demo 测试数据
-- 创建时间: YYYY-MM-DD

\c cleaning_talent_demo

-- 测试用户
INSERT INTO users (username, email, password_hash, full_name, department, position) VALUES
('admin', 'admin@lms.demo', '$2a$10$...', '系统管理员', 'IT部', '系统架构师'),
('test_user', 'test@lms.demo', '$2a$10$...', '测试用户', '销售部', '销售经理');

-- 更多测试数据...
```

---

## 验收标准

完成本会话后，应该能够：
- [ ] 成功连接到节点 A 的 PostgreSQL
- [ ] 数据库 `cleaning_talent_demo` 已创建
- [ ] 所有核心表已创建（6 张表）
- [ ] 索引已正确创建
- [ ] 测试数据已导入
- [ ] 后端可以成功连接数据库
- [ ] R03 后端工程师可以开始工作
