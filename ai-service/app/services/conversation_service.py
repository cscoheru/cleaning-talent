"""Conversation History Service"""
from typing import List, Dict, Optional
from datetime import datetime
import json


class ConversationService:
    """对话历史服务 - Demo 阶段使用内存存储"""

    def __init__(self, max_history: int = 10):
        """初始化对话服务

        Args:
            max_history: 每个用户最多保存的历史记录数量
        """
        self.max_history = max_history
        # Demo 阶段使用内存存储，生产环境应使用 Redis 或数据库
        self._storage: Dict[str, List[Dict]] = {}

    async def save_message(
        self,
        user_id: str,
        role: str,
        content: str,
        metadata: Optional[Dict] = None
    ) -> None:
        """保存一条消息

        Args:
            user_id: 用户 ID
            role: 角色 (user/assistant/system)
            content: 消息内容
            metadata: 额外的元数据（如 token 使用量）
        """
        if user_id not in self._storage:
            self._storage[user_id] = []

        message = {
            "role": role,
            "content": content,
            "timestamp": datetime.now().isoformat(),
            "metadata": metadata or {}
        }

        self._storage[user_id].append(message)

        # 保持历史记录在限制范围内
        if len(self._storage[user_id]) > self.max_history:
            self._storage[user_id] = self._storage[user_id][-self.max_history:]

    async def get_history(
        self,
        user_id: str,
        limit: Optional[int] = None
    ) -> List[Dict]:
        """获取用户对话历史

        Args:
            user_id: 用户 ID
            limit: 返回的记录数量限制

        Returns:
            对话历史列表
        """
        if user_id not in self._storage:
            return []

        history = self._storage[user_id]
        if limit:
            return history[-limit:]
        return history

    async def clear_history(self, user_id: str) -> None:
        """清除用户对话历史"""
        if user_id in self._storage:
            self._storage[user_id] = []

    def get_storage_stats(self) -> Dict:
        """获取存储统计信息（用于监控）"""
        return {
            "total_users": len(self._storage),
            "total_messages": sum(len(msgs) for msgs in self._storage.values()),
            "storage_type": "memory"
        }
