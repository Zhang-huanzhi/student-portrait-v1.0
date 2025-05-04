<template>
  <div class="chat-window">
    <div class="chat-messages" ref="messagesContainer">
      <div v-for="(message, index) in messages" :key="index" 
           :class="['message', message.type === 'user' ? 'user-message' : 'ai-message']">
        <div class="message-content">{{ message.content }}</div>
      </div>
    </div>
    <div class="chat-input">
      <el-input
        v-model="inputMessage"
        placeholder="请输入消息..."
        @keyup.enter="sendMessage"
      >
        <template #append>
          <el-button @click="sendMessage">发送</el-button>
        </template>
      </el-input>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue';
import { chatApi } from '../services/api';
import { ElMessage } from 'element-plus';

const messages = ref([]);
const inputMessage = ref('');
const messagesContainer = ref(null);
const userId = ref(localStorage.getItem('userId') || generateUserId());

function generateUserId() {
  const id = Math.random().toString(36).substring(2, 15);
  localStorage.setItem('userId', id);
  return id;
}

const sendMessage = async () => {
  if (!inputMessage.value.trim()) return;

  const userMessage = inputMessage.value;
  messages.value.push({ type: 'user', content: userMessage });
  inputMessage.value = '';

  await nextTick();
  scrollToBottom();

  try {
    const response = await chatApi.sendMessage(userMessage, userId.value);
    const aiMessage = response.data.answer || '抱歉，我无法回答这个问题。';
    messages.value.push({ type: 'ai', content: aiMessage });
  } catch (error) {
    ElMessage.error('发送消息失败');
    messages.value.push({ type: 'ai', content: '抱歉，发生了错误，请稍后重试。' });
  }

  await nextTick();
  scrollToBottom();
};

const scrollToBottom = () => {
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
  }
};

onMounted(() => {
  scrollToBottom();
});
</script>

<style scoped>
.chat-window {
  display: flex;
  flex-direction: column;
  height: 500px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
}

.message {
  margin-bottom: 10px;
  max-width: 80%;
}

.user-message {
  margin-left: auto;
}

.ai-message {
  margin-right: auto;
}

.message-content {
  padding: 10px;
  border-radius: 4px;
  background-color: #f4f4f5;
}

.user-message .message-content {
  background-color: #409eff;
  color: white;
}

.chat-input {
  padding: 10px;
  border-top: 1px solid #dcdfe6;
}
</style> 