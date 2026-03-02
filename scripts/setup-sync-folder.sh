#!/bin/bash
# setup-sync-folder.sh - 创建/重置 .claude-sync 文件夹结构
# 用途：初始化项目协作文件结构

set -e

PROJECT_ROOT="/Users/kjonekong/Documents/个人/cleaning_Talent"
SYNC_DIR="$PROJECT_ROOT/.claude-sync"

echo "=== 创建协作文件结构 ==="

# 创建 .claude-sync 目录
mkdir -p "$SYNC_DIR"

echo "目录创建完成: $SYNC_DIR"
echo ""
echo "已存在的协作文件:"
ls -la "$SYNC_DIR"
echo ""
echo "提示：此脚本用于初始化协作文件结构，"
echo "      如需重置所有协作文件，请手动删除 .claude-sync 文件夹后重新运行。"
echo ""
echo "下一步："
echo "1. 更新 DEPENDENCY-TREE.md，设定角色依赖"
echo "2. 更新 PROGRESS-BOARD.md，制定今日计划"
echo "3. 开始第一个 Claude Code 会话（R01 架构师）"
