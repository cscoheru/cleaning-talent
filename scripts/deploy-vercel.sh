#!/bin/bash
# deploy-vercel.sh - 部署前端到 Vercel
# 用途：将前端项目自动部署到 Vercel 平台

set -e

PROJECT_DIR="/Users/kjonekong/Documents/个人/cleaning_Talent"
FRONTEND_DIR="$PROJECT_DIR/frontend"

echo "=== 部署前端到 Vercel ==="

# 检查是否在项目根目录
if [ ! -d "$FRONTEND_DIR" ]; then
    echo "错误：前端目录不存在 ($FRONTEND_DIR)"
    echo "请先创建前端项目"
    exit 1
fi

cd "$FRONTEND_DIR"

# 检查是否已安装 Vercel CLI
if ! command -v vercel &> /dev/null; then
    echo "安装 Vercel CLI..."
    npm install -g vercel
fi

# 构建项目
echo "构建前端项目..."
npm run build

# 部署
echo "开始部署到 Vercel..."
vercel --prod

echo "=== 部署完成 ==="
echo "前端 URL 将在上方显示"
