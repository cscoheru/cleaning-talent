#!/bin/bash
# deploy-railway.sh - 部署后端到 Railway
# 用途：将后端项目自动部署到 Railway 平台

set -e

PROJECT_DIR="/Users/kjonekong/Documents/个人/cleaning_Talent"
BACKEND_DIR="$PROJECT_DIR/backend"

echo "=== 部署后端到 Railway ==="

# 检查是否在项目根目录
if [ ! -d "$BACKEND_DIR" ]; then
    echo "错误：后端目录不存在 ($BACKEND_DIR)"
    echo "请先创建后端项目"
    exit 1
fi

cd "$BACKEND_DIR"

# 检查是否已安装 Railway CLI
if ! command -v railway &> /dev/null; then
    echo "安装 Railway CLI..."
    npm install -g @railway/cli
fi

# 登录检查
railway status || railway login

# 构建（如果是 Java 项目）
if [ -f "build.gradle" ] || [ -f "build.gradle.kts" ]; then
    echo "构建后端项目..."
    ./gradlew clean build -x test --no-daemon
fi

# 部署到 Railway
echo "部署到 Railway..."
railway up

echo "=== 部署完成 ==="
echo "后端 URL: $(railway domain)"
