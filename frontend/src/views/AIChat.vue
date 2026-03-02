<template>
  <div class="ai-chat">
    <div class="chat-container">
      <!-- 消息列表 -->
      <div ref="messagesContainer" class="messages-list">
        <div
          v-for="message in messages"
          :key="message.id"
          class="message"
          :class="message.role"
        >
          <div class="message-avatar">
            <el-avatar v-if="message.role === 'assistant'" :size="36">
              <el-icon><MagicStick /></el-icon>
            </el-avatar>
            <el-avatar v-else :size="36" :src="userStore.userInfo?.avatarUrl">
              <el-icon><User /></el-icon>
            </el-avatar>
          </div>
          <div class="message-content">
            <div class="message-text" v-html="message.content"></div>
            <div class="message-time">{{ formatTime(message.timestamp) }}</div>
          </div>
        </div>

        <!-- 加载状态 -->
        <div v-if="isLoading" class="message assistant">
          <div class="message-avatar">
            <el-avatar :size="36">
              <el-icon><MagicStick /></el-icon>
            </el-avatar>
          </div>
          <div class="message-content">
            <div class="typing-indicator">
              <span></span>
              <span></span>
              <span></span>
            </div>
          </div>
        </div>
      </div>

      <!-- 输入区域 -->
      <div class="input-area">
        <div class="input-container">
          <el-input
            v-model="inputText"
            type="textarea"
            :rows="1"
            :autosize="{ minRows: 1, maxRows: 4 }"
            placeholder="输入你的问题..."
            @keydown.enter.exact="handleSend"
            @keydown.enter.shift.prevent="inputText += '\n'"
          />
          <el-button
            type="primary"
            :icon="Position"
            circle
            :loading="isLoading"
            :disabled="!inputText.trim()"
            @click="handleSend"
          />
        </div>
        <div class="input-hint">
          按 Enter 发送，Shift + Enter 换行
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { MagicStick, User, Position } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { aiChat } from '@/api/ai'

const route = useRoute()
const userStore = useUserStore()

const messagesContainer = ref<HTMLElement>()
const inputText = ref('')
const isLoading = ref(false)

interface Message {
  id: number
  role: 'user' | 'assistant'
  content: string
  timestamp: number
}

const messages = ref<Message[]>([
  {
    id: 1,
    role: 'assistant',
    content: '你好！我是 AI 学习助手，有什么可以帮助你的吗？你可以问我关于课程内容、技术问题或学习建议。',
    timestamp: Date.now()
  }
])

const formatTime = (timestamp: number) => {
  const date = new Date(timestamp)
  const hours = date.getHours().toString().padStart(2, '0')
  const minutes = date.getMinutes().toString().padStart(2, '0')
  return `${hours}:${minutes}`
}

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

const handleSend = async () => {
  const text = inputText.value.trim()
  if (!text || isLoading.value) return

  // 添加用户消息
  const userMessage: Message = {
    id: Date.now(),
    role: 'user',
    content: text.replace(/\n/g, '<br>'),
    timestamp: Date.now()
  }
  messages.value.push(userMessage)
  inputText.value = ''
  scrollToBottom()

  // 调用 AI API
  isLoading.value = true
  try {
    // TODO: 调用真实 API
    // const response = await aiChat({
    //   question: text,
    //   context: {
    //     courseId: Number(route.query.courseId),
    //     lessonId: Number(route.query.lessonId)
    //   }
    // })

    // Mock response
    await new Promise(resolve => setTimeout(resolve, 1500))
    const mockAnswer = generateMockAnswer(text)

    const assistantMessage: Message = {
      id: Date.now() + 1,
      role: 'assistant',
      content: mockAnswer,
      timestamp: Date.now()
    }
    messages.value.push(assistantMessage)
  } catch (error) {
    ElMessage.error('抱歉，AI 助手暂时无法响应，请稍后再试')
  } finally {
    isLoading.value = false
    scrollToBottom()
  }
}

const generateMockAnswer = (question: string) => {
  const lowerQuestion = question.toLowerCase()

  if (lowerQuestion.includes('vue') || lowerQuestion.includes('react')) {
    return 'Vue 3 和 React 都是优秀的前端框架。<br><br><strong>Vue 3 特点：</strong><br>• 组合式 API 更灵活<br>• 渐进式框架，学习曲线平缓<br>• 模板语法直观<br><br><strong>React 特点：</strong><br>• JSX 语法更灵活<br>• 生态更丰富<br>• 虚拟 DOM 性能优异<br><br>建议根据项目需求和个人偏好选择。'
  }

  if (lowerQuestion.includes('推荐') || lowerQuestion.includes('学什么')) {
    return '根据当前技术趋势，我推荐以下学习路径：<br><br>1️⃣ <strong>前端基础：</strong>HTML/CSS/JavaScript<br>2️⃣ <strong>框架选择：</strong>Vue 3 或 React<br>3️⃣ <strong>后端基础：</strong>Node.js 或 Python<br>4️⃣ <strong>数据库：</strong>MySQL 或 PostgreSQL<br><br>你可以从我们的课程列表中选择感兴趣的模块开始学习！'
  }

  return '感谢你的问题！这是一个很好的学习话题。<br><br>我建议你可以：<br>• 查看相关课程内容<br>• 在学习社区中讨论<br>• 实践练习加深理解<br><br>还有其他我可以帮助你的吗？'
}

onMounted(() => {
  scrollToBottom()
})
</script>

<style scoped>
.ai-chat {
  height: calc(100vh - 120px);
  display: flex;
  flex-direction: column;
}

.chat-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

/* 消息列表 */
.messages-list {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message {
  display: flex;
  gap: 12px;
  max-width: 80%;
}

.message.user {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.message-avatar {
  flex-shrink: 0;
}

.message-content {
  flex: 1;
  min-width: 0;
}

.message-text {
  padding: 12px 16px;
  border-radius: 16px;
  line-height: 1.6;
  word-wrap: break-word;
}

.message.assistant .message-text {
  background: #f1f5f9;
  color: #334155;
  border-bottom-left-radius: 4px;
}

.message.user .message-text {
  background: linear-gradient(135deg, #0d9488 0%, #0f766e 100%);
  color: #fff;
  border-bottom-right-radius: 4px;
}

.message-time {
  font-size: 11px;
  color: #94a3b8;
  margin-top: 4px;
  padding: 0 4px;
}

.message.user .message-time {
  text-align: right;
}

/* 加载动画 */
.typing-indicator {
  display: flex;
  gap: 4px;
  padding: 12px 16px;
  background: #f1f5f9;
  border-radius: 16px;
  border-bottom-left-radius: 4px;
  width: fit-content;
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  background: #94a3b8;
  border-radius: 50%;
  animation: typing 1.4s infinite;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
    opacity: 0.4;
  }
  30% {
    transform: translateY(-8px);
    opacity: 1;
  }
}

/* 输入区域 */
.input-area {
  padding: 16px 24px;
  border-top: 1px solid #e2e8f0;
  background: #fafafa;
}

.input-container {
  display: flex;
  gap: 12px;
  align-items: flex-end;
}

.input-container .el-textarea {
  flex: 1;
}

.input-container :deep(.el-textarea__inner) {
  border-radius: 16px;
  resize: none;
}

.input-hint {
  font-size: 12px;
  color: #94a3b8;
  text-align: center;
  margin-top: 8px;
}
</style>
