"""Health Check Endpoints"""
from fastapi import APIRouter
import time

router = APIRouter(prefix="/api/v1", tags=["系统"])


@router.get("/health")
async def health_check():
    """健康检查接口"""
    return {
        "status": "healthy",
        "service": "lms-ai-service",
        "timestamp": int(time.time() * 1000)
    }


@router.get("/ping")
async def ping():
    """Ping 接口 - 用于连通性测试"""
    return {"pong": True}
