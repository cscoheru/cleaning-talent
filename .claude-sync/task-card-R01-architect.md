# 📋 任务卡片：R01 架构师

## 角色信息

| 字段 | 值 |
|------|-----|
| **角色编号** | R01 |
| **角色名称** | 架构师 |
| **主要职责** | 技术选型、架构设计、API 规范、数据库设计 |
| **工作时长** | 4-6 小时 |
| **开始条件** | 无（可随时开始） |

---

## 会话目标

完成企业学习与人才管理一体化平台 Demo 阶段的系统架构设计，为后续开发奠定基础。

---

## 需要读取的文件

在开始工作前，请读取以下文件了解项目状态：

```bash
# 项目进度
cat .claude-sync/PROGRESS-BOARD.md

# 共享上下文
cat .claude-sync/SHARED-CONTEXT.md

# 依赖关系
cat .claude-sync/DEPENDENCY-TREE.md
```

---

## 任务清单

### 第一阶段：技术栈选型（1小时）

- [ ] 确定前端技术栈
  - 建议：Vue 3 + TypeScript + Vite + Element Plus
  - 理由：成熟稳定、生态丰富、国内文档多

- [ ] 确定后端技术栈
  - 建议：Spring Boot 3.x + Java 17 + MyBatis-Plus
  - 理由：企业级标准、微服务架构、人才储备多

- [ ] 确定 AI 服务技术栈
  - 建议：FastAPI + Python 3.11 + LangChain
  - 理由：AI 生态丰富、开发效率高

### 第二阶段：系统架构设计（2小时）

- [ ] 设计系统分层架构
  - 展示层（前端）
  - API 网关层
  - 业务服务层
  - 数据访问层
  - 基础设施层

- [ ] 设计模块划分
  - 用户认证模块
  - 课程管理模块
  - 学习地图模块
  - 积分系统模块
  - AI 问答模块
  - 人才评价模块

- [ ] 设计服务间通信方式
  - REST API（主要）
  - WebSocket（实时功能）

### 第三阶段：API 规范设计（1小时）

- [ ] 定义统一响应格式
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {}
  }
  ```

- [ ] 定义错误码规范
  - 400：请求参数错误
  - 401：未认证
  - 403：无权限
  - 500：服务器错误

- [ ] 定义核心接口列表（第一阶段）
  - 用户认证：登录、登出、刷新 Token
  - 课程管理：列表、详情、创建、更新、删除
  - 学习进度：记录、查询

### 第四阶段：数据库设计（1-1.5小时）

- [ ] 设计核心数据表
  - users（用户表）
  - courses（课程表）
  - user_learning_records（学习记录表）
  - learning_maps（学习地图表）
  - points_records（积分记录表）
  - ai_qa_logs（AI 问答日志表）

- [ ] 定义表关系
  - 一对多关系
  - 多对多关系
  - 外键约束

### 第五阶段：部署架构设计（30分钟）

- [ ] 设计 Demo 阶段部署架构
  - 前端：Vercel（免费）
  - 后端：Railway（免费额度）
  - AI 服务：节点 A Docker
  - 数据库：节点 A PostgreSQL（已有）

- [ ] 定义环境变量配置
  - 数据库连接
  - Redis 连接
  - MinIO 连接
  - AI API Key

---

## 输出文件

完成以下文件并保存到项目目录：

1. **`docs/architecture.md`** - 系统架构设计文档
   - 技术栈说明
   - 系统分层架构图
   - 模块划分说明

2. **`docs/api-spec.md`** - API 接口规范文档
   - 统一响应格式
   - 错误码定义
   - 核心接口列表

3. **`docs/database-design.md`** - 数据库设计文档
   - ER 图
   - 表结构定义
   - 字段说明

4. **`docs/deployment-plan.md`** - 部署计划文档
   - 部署架构图
   - 环境配置说明
   - 成本说明

---

## 完成后要更新的文件

```bash
# 1. 更新进度看板（标记完成）
# 编辑 .claude-sync/PROGRESS-BOARD.md

# 2. 更新共享上下文（记录技术决策）
# 编辑 .claude-sync/SHARED-CONTEXT.md

# 3. 更新会话日志（记录工作总结）
# 编辑 .claude-sync/SESSION-LOG.md

# 4. 更新变更日志
# 编辑 .claude-sync/CHANGE-LOG.md

# 5. 更新依赖树（解锁其他角色）
# 编辑 .claude-sync/DEPENDENCY-TREE.md
```

---

## 重要提醒

⚠️ **关键技术决策点**：
1. 前端选 Vue 3 而非 React - 团队可能更熟悉
2. 后端选 Spring Boot - 企业级标准
3. AI 服务选 FastAPI - Python AI 生态最佳
4. 部署优先 Vercel - 免费且简单

⚠️ **Demo 范围控制**：
- 暂不实现：实时评估（WebRTC 复杂度高）
- 暂不实现：九宫格人才地图（功能复杂）
- 优先实现：用户、课程、学习记录、基础 AI 问答

---

## 遇到问题时

1. **需要与其他角色协调**？
   - 在 `SESSION-LOG.md` 中记录问题
   - 在 `SHARED-CONTEXT.md` 中留言

2. **技术方案不确定**？
   - 参考 `docs/` 目录下的学习文档
   - 在 `SESSION-LOG.md` 中记录备选方案

3. **阻塞其他角色**？
   - 尽快完成核心部分
   - 在 `SHARED-CONTEXT.md` 中说明哪些已确定，哪些待定

---

## 验收标准

完成本会话后，应该能够：
- [ ] 清晰说明系统整体架构
- [ ] 列出所有核心模块及其职责
- [ ] 提供完整的 API 接口规范
- [ ] 提供完整的数据库表设计
- [ ] 说明 Demo 阶段的部署方案
- [ ] 其他角色（R02-R06）可以开始工作
