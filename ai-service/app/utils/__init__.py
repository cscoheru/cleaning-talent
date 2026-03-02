"""Utility Functions Package"""
import re
from typing import Optional


def sanitize_text(text: str, max_length: Optional[int] = None) -> str:
    """清理文本内容

    Args:
        text: 原始文本
        max_length: 最大长度限制

    Returns:
        清理后的文本
    """
    if not text:
        return ""

    # 移除控制字符
    text = re.sub(r'[\x00-\x1f\x7f-\x9f]', '', text)

    # 移除多余的空白字符
    text = re.sub(r'\s+', ' ', text).strip()

    # 应用长度限制
    if max_length and len(text) > max_length:
        text = text[:max_length] + "..."

    return text


def format_error_message(error: Exception, include_traceback: bool = False) -> str:
    """格式化错误信息

    Args:
        error: 异常对象
        include_traceback: 是否包含堆栈跟踪

    Returns:
        格式化后的错误信息
    """
    if include_traceback:
        import traceback
        return f"{type(error).__name__}: {str(error)}\n{traceback.format_exc()}"
    return f"{type(error).__name__}: {str(error)}"
