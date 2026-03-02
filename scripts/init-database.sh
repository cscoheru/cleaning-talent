#!/bin/bash
# init-database.sh - 初始化 Demo 数据库
# 用途：在节点 A 的 PostgreSQL 上创建数据库和初始表结构

set -e

NODE_A_HOST="139.224.42.111"
NODE_A_USER="root"
DB_NAME="cleaning_talent_demo"
DB_USER="postgres"
DB_PASS="WhjQTPAwInc5Vav3sDWe"
PROJECT_DIR="/Users/kjonekong/Documents/个人/cleaning_Talent"
SCHEMA_FILE="$PROJECT_DIR/backend/src/main/resources/schema.sql"
DATA_FILE="$PROJECT_DIR/backend/src/main/resources/data.sql"

echo "=== 初始化数据库 ==="

# 连接到节点 A 并创建数据库
echo "创建数据库: $DB_NAME"
ssh $NODE_A_USER@$NODE_A_HOST docker exec postgres psql -U postgres << EOF
-- 创建数据库
CREATE DATABASE $DB_NAME;

-- 连接到新数据库
\c $DB_NAME

-- 创建扩展
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 创建基础表（如果 schema.sql 存在）
$(test -f "$SCHEMA_FILE" && cat "$SCHEMA_FILE" || echo "-- schema.sql 尚未创建")

EOF

# 如果有数据文件，导入初始数据
if [ -f "$DATA_FILE" ]; then
    echo "导入初始数据..."
    ssh $NODE_A_USER@$NODE_A_HOST docker exec -i postgres psql -U postgres -d "$DB_NAME" < "$DATA_FILE"
else
    echo "提示：data.sql 尚未创建，跳过初始数据导入"
fi

echo ""
echo "=== 数据库初始化完成 ==="
echo ""
echo "数据库连接信息:"
echo "  Host: $NODE_A_HOST"
echo "  Port: 5432"
echo "  Database: $DB_NAME"
echo "  User: $DB_USER"
echo ""
echo "测试连接:"
echo "  ssh $NODE_A_USER@$NODE_A_HOST 'docker exec postgres psql -U $DB_USER -d $DB_NAME -c \"SELECT version();\"'"
