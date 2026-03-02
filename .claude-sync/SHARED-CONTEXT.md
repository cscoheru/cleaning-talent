# 共享上下文

## 项目信息

- **项目名称**: cleaning_Talent (企业学习与人才管理一体化平台)
- **阶段**: Demo/POC
- **开发模式**: 单人 + Claude Code 协作
- **工作目录**: `/Users/kjonekong/Documents/个人/cleaning_Talent`
- **GitHub**: (待创建)

## 技术栈（待 R01 确定）

| 层级 | 技术选择 | 版本 | 说明 |
|------|---------|------|------|
| 前端 | (待定) | - | - |
| 后端 | (待定) | - | - |
| AI | (待定) | - | - |

## API 规范（待 R01 定义）

```json
{
  "baseURL": "/api/v1",
  "responseFormat": {
    "code": 200,
    "message": "success",
    "data": {}
  }
}
```

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

## 部署信息（待 R06 配置）

```
前端:
  - 开发: localhost:5173
  - 预览: (待配置)

后端:
  - 开发: localhost:8080
  - 预览: (待配置)

AI 服务:
  - 开发: localhost:8000
  - 预览: (待配置)
```

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

| 待决策项 | 影响 | 负责角色 | 截止时间 |
|---------|------|---------|---------|
| 技术栈选型 | 整体架构 | R01 | Day 1 |
| 向量数据库选型 | AI 功能 | R05 | Day 3 |
| Demo 功能范围 | 开发范围 | R01 | Day 1 |

## 重要提醒

⚠️ **注意**：
- 任何技术决策变更必须先在此文档记录
- 其他角色查看此文档获取最新信息
- 决策有争议时由 R01（架构师）最终决定
- 每天开始工作前，请先查看此文档的最新状态
