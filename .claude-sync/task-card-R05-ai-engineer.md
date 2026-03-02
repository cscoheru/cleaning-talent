# 📋 任务卡片：R05 AI 工程师

## 角色信息

| 字段 | 值 |
|------|-----|
| **角色编号** | R05 |
| **角色名称** | AI 工程师 |
| **主要职责** | AI 功能开发、模型集成、向量数据库 |
| **工作时长** | 3-4 小时 |
| **开始条件** | 等待 R01 完成 AI 技术选型 |

---

## 会话目标

搭建 AI 服务框架，实现基础的 AI 问答功能，为后续智能化功能奠定基础。

---

## 依赖检查

**开始前请确认**：

```bash
# 检查 R01 AI 技术选型
cat docs/architecture.md | grep -i ai

# 检查共享上下文
cat .claude-sync/SHARED-CONTEXT.md
```

---

## 需要读取的文件

```bash
# 系统架构（了解 AI 服务定位）
cat docs/architecture.md

# 共享上下文
cat .claude-sync/SHARED-CONTEXT.md
```

---

## 任务清单

### 第一阶段：项目初始化（1小时）

- [ ] 创建 FastAPI 项目结构
  ```
  ai-service/
  ├── app/
  │   ├── __init__.py
  │   ├── main.py           # 应用入口
  │   ├── config.py         # 配置
  │   ├── api/              # 路由
  │   ├── services/         # 业务逻辑
  │   ├── models/           # 数据模型
  │   └── utils/            # 工具函数
  ├── requirements.txt      # 依赖
  ├── Dockerfile            # Docker 镜像
  └── pyproject.toml        # 项目配置
  ```

- [ ] 安装依赖
  ```bash
  pip install fastapi uvicorn
  pip install langchain langchain-openai
  pip install psycopg2-binary  # PostgreSQL
  pip install python-dotenv
  pip install pydantic
  ```

### 第二阶段：基础服务（30分钟）

- [ ] 创建 FastAPI 应用
  ```python
  # app/main.py
  from fastapi import FastAPI
  from fastapi.middleware.cors import CORSMiddleware

  app = FastAPI(title="LMS AI Service")

  app.add_middleware(
      CORSMiddleware,
      allow_origins=["*"],
      allow_credentials=True,
      allow_methods=["*"],
      allow_headers=["*"],
  )

  @app.get("/")
  async def root():
      return {"message": "LMS AI Service"}
  ```

- [ ] 配置环境变量
  ```python
  # app/config.py
  from pydantic_settings import BaseSettings

  class Settings(BaseSettings):
      DATABASE_URL: str
      OPENAI_API_KEY: str
      QWEN_API_KEY: str

      class Config:
          env_file = ".env"

  settings = Settings()
  ```

### 第三阶段：AI 问答功能（1.5小时）

- [ ] 创建问答接口
  ```python
  # app/api/qa.py
  from fastapi import APIRouter, HTTPException
  from pydantic import BaseModel

  router = APIRouter(prefix="/api/v1/qa", tags=["问答"])

  class QuestionRequest(BaseModel):
      question: str
      context: str = ""

  @router.post("/ask")
  async def ask_question(request: QuestionRequest):
      # TODO: 实现 AI 问答逻辑
      return {"answer": "这是 AI 的回答"}
  ```

- [ ] 集成通义千问
  ```python
  # app/services/llm_service.py
  from langchain.llms import Qwen
  from langchain.prompts import PromptTemplate

  class LLMService:
      def __init__(self, api_key: str):
          self.llm = Qwen(qwen_api_key=api_key)

      async def ask(self, question: str, context: str = "") -> str:
          prompt = f"""
          你是企业学习平台的 AI 助手，请回答以下问题：

          问题：{question}

          上下文：{context}

          请给出准确、友好的回答。
          """
          response = await self.llm.ainvoke(prompt)
          return response
  ```

- [ ] 创建对话历史存储
  ```python
  # app/services/conversation_service.py
  class ConversationService:
      def __init__(self, db_url: str):
          # 连接数据库存储对话历史
          pass

      async def save_message(self, user_id: str, role: str, content: str):
          # 保存消息到数据库
          pass

      async def get_history(self, user_id: str, limit: int = 10):
          # 获取对话历史
          pass
  ```

### 第四阶段：向量数据库准备（可选，30分钟）

- [ ] 研究向量数据库选型
  - Milvus（推荐）
  - Pinecone（云服务）
  - pgvector（PostgreSQL 扩展）

- [ ] 设计文档存储方案
  - 课程文档向量化
  - 语义搜索
  - RAG（检索增强生成）

---

## 输出文件

1. **`ai-service/requirements.txt`** - Python 依赖
2. **`ai-service/app/main.py`** - FastAPI 应用入口
3. **`ai-service/app/config.py`** - 配置
4. **`ai-service/app/api/qa.py`** - 问答接口
5. **`ai-service/app/services/llm_service.py`** - LLM 服务
6. **`ai-service/Dockerfile`** - Docker 配置
7. **`ai-service/.env.example`** - 环境变量模板

---

## 完成后要更新的文件

```bash
# 1. 更新进度看板
# 编辑 .claude-sync/PROGRESS-BOARD.md

# 2. 更新代码所有权
# 编辑 .claude-sync/CODE-OWNERSHIP.md
# 声明 /ai-service/ 为 R05 负责

# 3. 更新共享上下文
# 编辑 .claude-sync/SHARED-CONTEXT.md
# 记录 AI 服务地址

# 4. 更新会话日志
# 编辑 .claude-sync/SESSION-LOG.md

# 5. 更新变更日志
# 编辑 .claude-sync/CHANGE-LOG.md
```

---

## 重要提醒

⚠️ **API Key 配置**：
- 通义千问 API Key 需要自己申请
- 在 `ai-service/.env` 中配置：
  ```
  QWEN_API_KEY=your_api_key_here
  DATABASE_URL=postgresql://postgres:password@139.224.42.111:5432/cleaning_talent_demo
  ```

⚠️ **Demo 阶段简化**：
- 不实现复杂的 RAG
- 不实现向量数据库
- 使用简单的 Prompt Engineering
- 重点验证 AI 集成可行性

⚠️ **成本控制**：
- 通义千问按 Token 计费
- 注意控制 Prompt 长度
- Demo 阶段可以设置用量限制

---

## 代码示例

### 完整的问答接口

```python
# app/api/qa.py
from fastapi import APIRouter, Depends, HTTPException
from pydantic import BaseModel
from app.services.llm_service import LLMService
from app.config import settings

router = APIRouter(prefix="/api/v1/qa", tags=["问答"])

class QuestionRequest(BaseModel):
    question: str
    context: str = ""
    user_id: str = "anonymous"

class QuestionResponse(BaseModel):
    answer: str
    usage: dict = {}

llm_service = LLMService(api_key=settings.QWEN_API_KEY)

@router.post("/ask", response_model=QuestionResponse)
async def ask_question(request: QuestionRequest):
    """
    AI 问答接口

    - **question**: 用户问题
    - **context**: 可选的上下文信息
    - **user_id**: 用户 ID（可选）
    """
    if not request.question:
        raise HTTPException(status_code=400, detail="问题不能为空")

    try:
        answer = await llm_service.ask(
            question=request.question,
            context=request.context
        )
        return QuestionResponse(answer=answer)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"AI 服务错误: {str(e)}")

@router.get("/health")
async def health_check():
    """健康检查"""
    return {"status": "healthy", "service": "ai-qa"}
```

### Dockerfile

```dockerfile
# ai-service/Dockerfile
FROM python:3.11-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY app ./app

CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]
```

---

## 开发流程

1. **本地开发**
   ```bash
   cd ai-service
   python -m venv venv
   source venv/bin/activate  # Windows: venv\Scripts\activate
   pip install -r requirements.txt
   uvicorn app.main:app --reload
   ```

2. **访问 API 文档**
   - Swagger UI: http://localhost:8000/docs
   - ReDoc: http://localhost:8000/redoc

3. **测试问答接口**
   ```bash
   curl -X POST http://localhost:8000/api/v1/qa/ask \
     -H "Content-Type: application/json" \
     -d '{"question":"什么是学习地图？"}'
   ```

---

## 验收标准

完成本会话后，应该能够：
- [ ] AI 服务可以在本地启动
- [ ] 可以访问 Swagger API 文档
- [ ] 问答接口可以返回 AI 回答
- [ ] 回答与问题相关（准确性验证）
- [ ] 错误处理正常
- [ ] Docker 镜像可以构建成功
