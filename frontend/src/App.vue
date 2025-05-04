<template>
  <div class="app-container">
    <el-container>
      <el-header class="header-gradient">
        <img src="/logo.png" class="logo" />
        <span class="title">学生用户画像管理系统</span>
      </el-header>
      <el-main>
        <el-row :gutter="20">
          <el-col :span="14">
            <el-card class="main-card">
              <StudentList />
            </el-card>
          </el-col>
          <el-col :span="10">
            <el-card class="main-card chat-card">
              <iframe
                src="https://udify.app/chatbot/qsLFVCR86iYCKwSi"
                style="width: 100%; height: 100%; min-height: 700px; border: none; border-radius: 12px; box-shadow: 0 2px 12px #b3c0d1;"
                allow="microphone"
              ></iframe>
            </el-card>
          </el-col>
        </el-row>
        <el-row style="margin-top: 32px">
          <el-col :span="24">
            <KnowledgeSegmentManager />
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import StudentList from './components/StudentList.vue';
import KnowledgeSegmentManager from './components/KnowledgeSegmentManager.vue';
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
  box-shadow: 0 2px 8px #dbeafe;
}

.logo {
  height: 40px;
  margin-right: 16px;
}

.title {
  font-size: 2rem;
  font-weight: bold;
  letter-spacing: 2px;
}

.el-main {
  padding: 32px 0;
}

.main-card {
  border-radius: 16px;
  box-shadow: 0 4px 24px #dbeafe;
  min-height: 720px;
  background: #fff;
}

.chat-card {
  min-height: 720px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}
</style> 