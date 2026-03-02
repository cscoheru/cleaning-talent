"""LLM Service - Qwen Integration"""
import time
from typing import Optional
import dashscope
from dashscope import Generation

from app.config import settings


class LLMService:
    """通义千问 LLM 服务"""

    def __init__(self, api_key: str):
        """初始化 LLM 服务

        Args:
            api_key: 通义千问 API Key
        """
        dashscope.api_key = api_key
        self.model = settings.DEFAULT_MODEL

    async def ask(
        self,
        question: str,
        context: str = "",
        model: Optional[str] = None
    ) -> tuple[str, dict]:
        """调用 LLM 进行问答

        Args:
            question: 用户问题
            context: 上下文信息（可选）
            model: 使用的模型（可选，默认使用配置的模型）

        Returns:
            (answer, usage) - AI 回答内容和用量信息
        """
        model_to_use = model or self.model

        # 构建 system prompt 和 user prompt
        system_prompt = self._build_system_prompt()
        user_prompt = self._build_user_prompt(question, context)

        try:
            response = Generation.call(
                model=model_to_use,
                messages=[
                    {"role": "system", "content": system_prompt},
                    {"role": "user", "content": user_prompt}
                ],
                result_format="message",
            )

            if response.status_code == 200:
                answer = response.output.choices[0].message.content
                usage = {
                    "prompt_tokens": response.usage.input_tokens,
                    "completion_tokens": response.usage.output_tokens,
                    "total_tokens": response.usage.input_tokens + response.usage.output_tokens,
                    "model": model_to_use
                }
                return answer, usage
            else:
                error_msg = response.message if hasattr(response, 'message') else f"API error: {response.status_code}"
                raise Exception(f"通义千问 API 调用失败: {error_msg}")

        except Exception as e:
            raise Exception(f"LLM 服务错误: {str(e)}")

    def _build_system_prompt(self) -> str:
        """构建系统提示词"""
        return """你是企业学习与人才管理平台的 AI 助手。你的职责是：

1. 回答用户关于学习、课程、技能发展的问题
2. 提供专业、准确、友好的回答
3. 当不确定答案时，诚实告知用户
4. 保持回答简洁但全面

回答风格：
- 使用清晰的中文表达
- 适当使用列表和分点说明
- 当涉及专业概念时，提供简要解释"""

    def _build_user_prompt(self, question: str, context: str = "") -> str:
        """构建用户提示词"""
        if context:
            return f"""上下文信息：
{context}

用户问题：
{question}

请基于以上信息回答用户的问题。"""
        else:
            return f"""用户问题：
{question}

请回答这个问题。"""


class MockLLMService:
    """Mock LLM 服务 - 用于测试和开发环境"""

    async def ask(
        self,
        question: str,
        context: str = "",
        model: Optional[str] = None
    ) -> tuple[str, dict]:
        """模拟 LLM 问答"""
        # 简单的关键词匹配模拟
        answers = {
            "学习地图": "学习地图（Learning Map）是一种结构化的学习路径规划工具，它帮助员工根据自身职业目标和能力差距，规划个性化的学习课程和发展路径。",
            "课程": "课程平台提供了丰富的学习资源，包括在线视频课程、文档资料和实践项目。您可以按需选择适合的课程进行学习。",
            "积分": "积分系统用于激励学习行为。完成课程学习、通过考试、参与活动都可以获得积分，积分可以兑换奖励和福利。",
            "技能": "技能发展是平台的核心功能之一。通过学习课程、参加培训和实践项目，您可以不断提升专业技能和软技能。",
        }

        # 简单的关键词匹配
        for keyword, answer in answers.items():
            if keyword in question:
                usage = {
                    "prompt_tokens": len(question),
                    "completion_tokens": len(answer),
                    "total_tokens": len(question) + len(answer),
                    "model": "mock"
                }
                return answer, usage

        # 默认回答
        default_answer = f"感谢您的问题「{question}」。这是一个很好的问题！作为 AI 助手，我目前处于模拟模式，建议您联系平台管理员获取更详细的帮助。"
        usage = {
            "prompt_tokens": len(question),
            "completion_tokens": len(default_answer),
            "total_tokens": len(question) + len(default_answer),
            "model": "mock"
        }
        return default_answer, usage
