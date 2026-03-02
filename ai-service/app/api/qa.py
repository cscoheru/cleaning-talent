"""Q&A API Endpoints"""
import time
from fastapi import APIRouter, HTTPException, Request
from fastapi.responses import JSONResponse

from app.config import settings
from app.models import QuestionRequest, QuestionResponse
from app.services.llm_service import LLMService, MockLLMService
from app.services.conversation_service import ConversationService

router = APIRouter(prefix="/api/v1/qa", tags=["问答"])

# Initialize services
# 如果没有配置 API Key，使用 Mock 服务
if settings.QWEN_API_KEY:
    llm_service = LLMService(api_key=settings.QWEN_API_KEY)
else:
    llm_service = MockLLMService()

conversation_service = ConversationService(max_history=settings.MAX_HISTORY_LENGTH)


def create_response(data: dict) -> dict:
    """创建统一格式的响应"""
    return {
        "code": 200,
        "message": "success",
        "data": data,
        "timestamp": int(time.time() * 1000)
    }


def create_error_response(code: int, message: str, errors: list = None) -> dict:
    """创建统一格式的错误响应"""
    return {
        "code": code,
        "message": message,
        "data": None,
        "timestamp": int(time.time() * 1000),
        "errors": errors or []
    }


@router.post("/ask", response_model=QuestionResponse)
async def ask_question(request: QuestionRequest, http_request: Request):
    """AI 问答接口

    Args:
        request: 问题请求对象
        http_request: FastAPI Request 对象

    Returns:
        QuestionResponse: AI 回答响应

    请求示例:
        POST /api/v1/qa/ask
        {
            "question": "什么是学习地图？",
            "context": "企业学习平台",
            "user_id": "user_123"
        }

    响应示例:
        {
            "code": 200,
            "message": "success",
            "data": {
                "answer": "学习地图是...",
                "model": "qwen-turbo",
                "usage": {...}
            },
            "timestamp": 1709356800000
        }
    """
    # 验证问题不为空
    if not request.question or not request.question.strip():
        raise HTTPException(
            status_code=400,
            detail=create_error_response(400, "问题不能为空", ["question field is required"])
        )

    # 验证问题长度
    if len(request.question) > settings.MAX_QUESTION_LENGTH:
        raise HTTPException(
            status_code=400,
            detail=create_error_response(
                400,
                f"问题长度超过限制（最大 {settings.MAX_QUESTION_LENGTH} 字符）",
                ["question too long"]
            )
        )

    # 验证上下文长度
    if len(request.context) > settings.MAX_CONTEXT_LENGTH:
        raise HTTPException(
            status_code=400,
            detail=create_error_response(
                400,
                f"上下文长度超过限制（最大 {settings.MAX_CONTEXT_LENGTH} 字符）",
                ["context too long"]
            )
        )

    try:
        # 保存用户问题到历史
        await conversation_service.save_message(
            user_id=request.user_id,
            role="user",
            content=request.question
        )

        # 调用 LLM 服务
        answer, usage = await llm_service.ask(
            question=request.question,
            context=request.context
        )

        # 保存 AI 回答到历史
        await conversation_service.save_message(
            user_id=request.user_id,
            role="assistant",
            content=answer,
            metadata=usage
        )

        # 构建响应
        response_data = {
            "answer": answer,
            "user_id": request.user_id,
            "usage": usage
        }

        return JSONResponse(content=create_response(response_data))

    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=create_error_response(
                500,
                "AI 服务错误",
                [str(e) if settings.DEBUG else "Internal server error"]
            )
        )


@router.get("/history/{user_id}")
async def get_conversation_history(user_id: str, limit: int = 10):
    """获取用户对话历史

    Args:
        user_id: 用户 ID
        limit: 返回的历史记录数量（默认 10）

    Returns:
        对话历史列表
    """
    if limit <= 0 or limit > 50:
        raise HTTPException(
            status_code=400,
            detail=create_error_response(
                400,
                "limit 必须在 1-50 之间",
                ["invalid limit parameter"]
            )
        )

    history = await conversation_service.get_history(user_id=user_id, limit=limit)

    return JSONResponse(content=create_response({
        "user_id": user_id,
        "history": history,
        "count": len(history)
    }))


@router.delete("/history/{user_id}")
async def clear_conversation_history(user_id: str):
    """清除用户对话历史

    Args:
        user_id: 用户 ID

    Returns:
        操作结果
    """
    await conversation_service.clear_history(user_id=user_id)

    return JSONResponse(content=create_response({
        "user_id": user_id,
        "cleared": True
    }))


@router.get("/health")
async def health_check():
    """问答服务健康检查"""
    stats = conversation_service.get_storage_stats()

    return JSONResponse(content=create_response({
        "status": "healthy",
        "service": "ai-qa",
        "llm_service": "qwen" if settings.QWEN_API_KEY else "mock",
        "storage_stats": stats
    }))
