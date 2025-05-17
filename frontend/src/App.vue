<template>
  <div class="app-container">
    <el-container>
      <el-header class="header-gradient">
        <img src="/logo.png" class="logo" />
        <span class="title">学生用户画像管理系统</span>
      </el-header>
      <el-main>        <el-row :gutter="20">          <el-col :span="14">
            <el-card class="main-card">
              <div class="section-header">
                <el-icon><User /></el-icon>
                <h3>学生信息管理</h3>
              </div>
              <StudentList />
            </el-card>
          </el-col><el-col :span="10">
            <el-card class="main-card chat-card">
              <div class="chat-header">
                <el-icon><ChatDotSquare /></el-icon>
                <h3>用户画像Agent</h3>
              </div>
              <iframe
                src="https://udify.app/chatbot/qsLFVCR86iYCKwSi"
                style="width: 100%; height: 100%; min-height: 680px; border: none; border-radius: 12px; box-shadow: 0 2px 12px #b3c0d1;"
                allow="microphone"
              ></iframe>
            </el-card>
          </el-col>
        </el-row>        <el-row style="margin-top: 32px">
          <el-col :span="24">
            <el-card class="main-card">
              <div class="section-header">
                <el-icon><Reading /></el-icon>
                <h3>知识段管理</h3>
              </div>
              <KnowledgeSegmentManager />
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import StudentList from './components/StudentList.vue';
import KnowledgeSegmentManager from './components/KnowledgeSegmentManager.vue';
import { ChatDotSquare, Reading, User } from '@element-plus/icons-vue';
</script>

<style>
.app-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #e0e7ff 0%, #f0f9ff 100%);
}

.header-gradient {
  background: linear-gradient(90deg, #409eff 0%, #66b1ff 100%);
  color: #fff;
  display: flex;
  align-items: center;
  height: 64px;
  box-shadow: 0 2px 8px rgba(0, 120, 212, 0.2);
  position: sticky;
  top: 0;
  z-index: 10;
  animation: slideDown 0.5s ease;
}

@keyframes slideDown {
  from {
    transform: translateY(-100%);
  }
  to {
    transform: translateY(0);
  }
}

.logo {
  height: 40px;
  margin-right: 16px;
  animation: fadeIn 1s ease;
  filter: drop-shadow(0 2px 5px rgba(0, 0, 0, 0.2));
}

.title {
  font-size: 2rem;
  font-weight: bold;
  letter-spacing: 2px;
  animation: fadeIn 1s ease;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.el-main {
  padding: 32px 0;
}

.main-card {
  border-radius: 16px;
  box-shadow: 0 6px 30px rgba(0, 120, 212, 0.15);
  min-height: 720px;
  background: #fff;
  transition: all 0.3s ease;
  border: none;
  overflow: hidden;
  animation: cardFadeIn 0.8s ease;
  animation-fill-mode: both;
}

@keyframes cardFadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.main-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 36px rgba(0, 120, 212, 0.2);
}

.chat-card {
  min-height: 720px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.chat-header {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background: linear-gradient(90deg, #409eff 0%, #66b1ff 100%);
  color: #fff;
  border-radius: 8px 8px 0 0;
  margin-bottom: 10px;
}

.chat-header h3 {
  margin: 0 0 0 10px;
  font-weight: 600;
  font-size: 18px;
}

.chat-header .el-icon {  font-size: 20px;
}

.section-header {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background: linear-gradient(90deg, #409eff 0%, #66b1ff 100%);
  color: #fff;
  border-radius: 8px 8px 0 0;
  margin-bottom: 16px;
}

.section-header h3 {
  margin: 0 0 0 10px;
  font-weight: 600;
  font-size: 18px;
}
</style> 