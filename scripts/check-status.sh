#!/bin/bash
# check-status.sh - 检查项目状态
# 用途：每天开始工作前运行，检查项目当前状态

SYNC_DIR="/Users/kjonekong/Documents/个人/cleaning_Talent/.claude-sync"
PROJECT_DIR="/Users/kjonekong/Documents/个人/cleaning_Talent"

echo "======================================"
echo "       项目状态检查"
echo "======================================"
echo ""

# 1. 依赖关系状态
echo "【1. 依赖关系状态】"
if [ -f "$SYNC_DIR/DEPENDENCY-TREE.md" ]; then
    cat "$SYNC_DIR/DEPENDENCY-TREE.md" | grep -A 10 "## 当前状态" || echo "   尚未定义状态"
else
    echo "   文件不存在"
fi
echo ""

# 2. 今日进度
echo "【2. 今日进度】"
if [ -f "$SYNC_DIR/PROGRESS-BOARD.md" ]; then
    cat "$SYNC_DIR/PROGRESS-BOARD.md" | grep -A 15 "### 今日任务" || echo "   尚未定义今日任务"
else
    echo "   文件不存在"
fi
echo ""

# 3. 锁定状态
echo "【3. 锁定状态】"
if [ -f "$SYNC_DIR/CODE-OWNERSHIP.md" ]; then
    LOCK_COUNT=$(cat "$SYNC_DIR/CODE-OWNERSHIP.md" | grep "🔒" | wc -l | tr -d ' ')
    if [ "$LOCK_COUNT" -gt 0 ]; then
        echo "   当前锁定文件数: $LOCK_COUNT"
        cat "$SYNC_DIR/CODE-OWNERSHIP.md" | grep "🔒" | while read line; do echo "   $line"; done
    else
        echo "   无锁定"
    fi
else
    echo "   文件不存在"
fi
echo ""

# 4. 待处理提醒
echo "【4. 重要提醒】"
if [ -f "$SYNC_DIR/SHARED-CONTEXT.md" ]; then
    ALERT_COUNT=$(cat "$SYNC_DIR/SHARED-CONTEXT.md" | grep "⚠️" | wc -l | tr -d ' ')
    if [ "$ALERT_COUNT" -gt 0 ]; then
        echo "   重要提醒数: $ALERT_COUNT"
        cat "$SYNC_DIR/SHARED-CONTEXT.md" | grep "⚠️" | while read line; do echo "   $line"; done
    else
        echo "   无待处理提醒"
    fi
else
    echo "   文件不存在"
fi
echo ""

# 5. 未提交更改
echo "【5. Git 状态】"
if [ -d "$PROJECT_DIR/.git" ]; then
    cd "$PROJECT_DIR"
    UNCOMITTED=$(git status --short 2>/dev/null | wc -l | tr -d ' ')
    if [ "$UNCOMITTED" -gt 0 ]; then
        echo "   未提交文件数: $UNCOMITTED"
        git status --short
    else
        echo "   工作目录干净"
    fi
else
    echo "   尚未初始化 Git 仓库"
fi
echo ""

echo "======================================"
echo "       检查完成"
echo "======================================"
