"""Data Models Package"""
from pydantic import BaseModel, Field
from typing import Optional


class QuestionRequest(BaseModel):
    """用户问题请求"""
    question: str = Field(..., min_length=1, max_length=1000, description="用户问题")
    context: str = Field(default="", max_length=5000, description="可选的上下文信息")
    user_id: str = Field(default="anonymous", description="用户 ID")

    class Config:
        json_schema_extra = {
            "example": {
                "question": "什么是学习地图？",
                "context": "企业学习平台相关",
                "user_id": "user_123"
            }
        }


class QuestionResponse(BaseModel):
    """AI 回答响应"""
    code: int = 200
    message: str = "success"
    data: dict
    timestamp: int

    class Config:
        json_schema_extra = {
            "example": {
                "code": 200,
                "message": "success",
                "data": {
                    "answer": "学习地图是一种结构化的学习路径规划工具...",
                    "model": "qwen-turbo",
                    "usage": {"prompt_tokens": 100, "completion_tokens": 200}
                },
                "timestamp": 1709356800000
            }
        }


class ErrorResponse(BaseModel):
    """错误响应"""
    code: int
    message: str
    data: None
    timestamp: int
    errors: list[str] = []
