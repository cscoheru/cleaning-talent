"""Application Configuration"""
from pydantic_settings import BaseSettings, SettingsConfigDict
from pydantic import field_validator
from functools import lru_cache


class Settings(BaseSettings):
    """Application settings"""

    # Application
    APP_NAME: str = "LMS AI Service"
    APP_VERSION: str = "0.1.0"
    DEBUG: bool = False

    # Server
    HOST: str = "0.0.0.0"
    PORT: int = 8000

    # Database
    DATABASE_URL: str = "postgresql://postgres:password@localhost:5432/cleaning_talent_demo"

    # AI Services
    QWEN_API_KEY: str = ""
    OPENAI_API_KEY: str = ""
    DEFAULT_MODEL: str = "qwen-turbo"  # qwen-turbo, qwen-plus, qwen-max

    # CORS - 需要从逗号分隔的字符串解析
    CORS_ORIGINS: str = "http://localhost:5173,http://localhost:8080,https://lms-demo.vercel.app,https://lms-backend.railway.app"

    # Conversation History
    MAX_HISTORY_LENGTH: int = 10

    # Rate Limiting (Demo protection)
    MAX_QUESTION_LENGTH: int = 1000
    MAX_CONTEXT_LENGTH: int = 5000

    model_config = SettingsConfigDict(
        env_file=".env",
        case_sensitive=True,
        extra="ignore"
    )

    @property
    def cors_origins_list(self) -> list[str]:
        """将 CORS_ORIGINS 字符串转换为列表"""
        return [origin.strip() for origin in self.CORS_ORIGINS.split(",") if origin.strip()]


@lru_cache()
def get_settings() -> Settings:
    """Get cached settings instance"""
    return Settings()


settings = get_settings()
