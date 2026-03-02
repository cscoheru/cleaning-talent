#!/bin/bash
# deploy-to-aliyun.sh - 部署 AI 服务到节点 A
# 用途：将 AI 服务部署到阿里云服务器

set -e

NODE_A_USER="root"
NODE_A_HOST="139.224.42.111"
PROJECT_PATH="/opt/lms-demo/ai-service"
PROJECT_DIR="/Users/kjonekong/Documents/个人/cleaning_Talent"
AI_SERVICE_DIR="$PROJECT_DIR/ai-service"

echo "=== 部署 AI 服务到节点 A ==="

# 检查 AI 服务目录
if [ ! -d "$AI_SERVICE_DIR" ]; then
    echo "错误：AI 服务目录不存在 ($AI_SERVICE_DIR)"
    exit 1
fi

# 上传代码到节点 A
echo "上传代码到节点 A..."
rsync -avz --progress \
  --exclude '__pycache__' \
  --exclude '*.pyc' \
  --exclude '.DS_Store' \
  --exclude '.git' \
  --exclude 'venv' \
  "$AI_SERVICE_DIR/" \
  $NODE_A_USER@$NODE_A_HOST:$PROJECT_PATH/

# 在节点 A 上构建和启动
echo "在节点 A 上构建和启动容器..."
ssh $NODE_A_USER@$NODE_A_HOST << 'ENDSSH'
cd $PROJECT_PATH

# 检查是否存在 Dockerfile
if [ ! -f "Dockerfile" ]; then
    echo "警告：Dockerfile 不存在，跳过容器构建"
    exit 0
fi

# 构建镜像
echo "构建 Docker 镜像..."
docker build -t lms-ai-service:latest .

# 停止旧容器
echo "停止旧容器..."
docker stop lms-ai-service || true
docker rm lms-ai-service || true

# 启动新容器
echo "启动新容器..."
docker run -d \
  --name lms-ai-service \
  -p 8000:8000 \
  --restart always \
  -e PYTHONUNBUFFERED=1 \
  lms-ai-service:latest

echo "AI 服务已启动"
docker ps | grep lms-ai-service
ENDSSH

echo ""
echo "=== 部署完成 ==="
echo "AI 服务 URL: http://$NODE_A_HOST:8000"
echo "API 文档: http://$NODE_A_HOST:8000/docs"
echo ""
echo "查看日志: ssh $NODE_A_USER@$NODE_A_HOST 'docker logs -f lms-ai-service'"
