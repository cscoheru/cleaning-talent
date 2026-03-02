# LMS AI Service

企业学习与人才管理平台的 AI 服务模块，提供智能问答功能。

## 技术栈

- **Python 3.11+**
- **FastAPI 0.109+** - 现代高性能 Web 框架
- **通义千问 API** - 阿里云大语言模型服务
- **LangChain** - LLM 编排框架

## 功能特性

- ✅ AI 智能问答
- ✅ 对话历史管理（内存存储）
- ✅ 统一响应格式
- ✅ 健康检查接口
- ✅ Mock 模式（开发测试）

## 快速开始

### 1. 安装依赖

```bash
# 创建虚拟环境
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate

# 安装依赖
pip install -r requirements.txt
```

### 2. 配置环境变量

```bash
# 复制环境变量模板
cp .env.example .env

# 编辑 .env 文件，填写通义千问 API Key
# QWEN_API_KEY=your_api_key_here
```

### 3. 启动服务

```bash
# 开发模式（自动重载）
uvicorn app.main:app --reload --port 8000

# 生产模式
uvicorn app.main:app --host 0.0.0.0 --port 8000 --workers 4
```

### 4. 访问 API 文档

- Swagger UI: http://localhost:8000/docs
- ReDoc: http://localhost:8000/redoc

## API 接口

### 1. 健康检查

```bash
GET /api/v1/health
```

### 2. AI 问答

```bash
POST /api/v1/qa/ask
Content-Type: application/json

{
  "question": "什么是学习地图？",
  "context": "企业学习平台相关",
  "user_id": "user_123"
}
```

**响应格式：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "answer": "学习地图是...",
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

### 3. 获取对话历史

```bash
GET /api/v1/qa/history/{user_id}?limit=10
```

### 4. 清除对话历史

```bash
DELETE /api/v1/qa/history/{user_id}
```

## Docker 部署

### 构建镜像

```bash
docker build -t lms-ai-service:latest .
```

### 运行容器

```bash
docker run -d \
  --name lms-ai-service \
  -p 8000:8000 \
  --env-file .env \
  lms-ai-service:latest
```

### Docker Compose

```yaml
version: '3.8'
services:
  ai-service:
    build: .
    ports:
      - "8000:8000"
    environment:
      - QWEN_API_KEY=${QWEN_API_KEY}
      - DATABASE_URL=${DATABASE_URL}
    restart: unless-stopped
```

## 项目结构

```
ai-service/
├── app/
│   ├── __init__.py           # 包初始化
│   ├── main.py               # FastAPI 应用入口
│   ├── config.py             # 配置管理
│   ├── api/                  # API 路由
│   │   ├── __init__.py
│   │   ├── health.py         # 健康检查
│   │   └── qa.py             # 问答接口
│   ├── services/             # 业务逻辑
│   │   ├── __init__.py
│   │   ├── llm_service.py    # LLM 服务
│   │   └── conversation_service.py  # 对话历史
│   ├── models/               # 数据模型
│   │   └── __init__.py
│   └── utils/                # 工具函数
│       └── __init__.py
├── requirements.txt          # Python 依赖
├── Dockerfile                # Docker 配置
├── .env.example              # 环境变量模板
├── pyproject.toml            # 项目配置
└── README.md                 # 项目文档
```

## 配置说明

| 环境变量 | 说明 | 默认值 |
|---------|------|--------|
| `QWEN_API_KEY` | 通义千问 API Key | 空（使用 Mock） |
| `DATABASE_URL` | PostgreSQL 连接字符串 | 本地数据库 |
| `DEFAULT_MODEL` | 默认模型 | `qwen-turbo` |
| `MAX_HISTORY_LENGTH` | 最大历史记录数 | 10 |
| `MAX_QUESTION_LENGTH` | 问题最大长度 | 1000 |
| `MAX_CONTEXT_LENGTH` | 上下文最大长度 | 5000 |

## Mock 模式

当未配置 `QWEN_API_KEY` 时，服务将使用 Mock 模式，返回预设的模拟回答。

**适用于：**
- 开发测试
- API 调试
- 前端集成

## 测试

```bash
# 测试问答接口
curl -X POST http://localhost:8000/api/v1/qa/ask \
  -H "Content-Type: application/json" \
  -d '{"question":"什么是学习地图？"}'

# 测试健康检查
curl http://localhost:8000/api/v1/health
```

## 部署

### Node A 部署

```bash
# 1. 构建 Docker 镜像
docker build -t lms-ai-service:latest .

# 2. 运行容器
docker run -d \
  --name lms-ai-service \
  -p 8000:8000 \
  --env-file .env \
  --restart unless-stopped \
  lms-ai-service:latest

# 3. 验证服务
curl http://139.224.42.111:8000/api/v1/health
```

## 常见问题

**Q: 如何获取通义千问 API Key？**

A: 访问 https://dashscope.aliyun.com 注册并获取 API Key。

**Q: 对话历史存储在哪里？**

A: Demo 阶段使用内存存储，重启后丢失。生产环境建议使用 Redis。

**Q: 如何更换其他 LLM？**

A: 在 `app/services/llm_service.py` 中实现相应的服务类。

## License

MIT
