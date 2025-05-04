<template>
  <el-card>
    <h2>知识库分段管理</h2>
    <el-form label-width="120px">
      <el-form-item label="Dify API Key">
        <el-input v-model="apiKey" placeholder="请输入Dify API Key" show-password />
      </el-form-item>
      <el-form-item label="Dataset ID">
        <el-input v-model="datasetId" placeholder="请输入Dataset ID" />
      </el-form-item>
      <el-form-item label="文档ID (Document ID)">
        <el-input v-model="documentId" placeholder="请输入目标文档ID" />
      </el-form-item>
      <el-form-item label="分段内容">
        <el-input
          type="textarea"
          v-model="segmentText"
          :rows="6"
          placeholder="请输入要添加到知识库文档的分段内容"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="addSegment" :loading="loading">添加分段</el-button>
      </el-form-item>
    </el-form>
    <el-alert v-if="result" :title="result" type="success" show-icon />
    <el-alert v-if="error" :title="error" type="error" show-icon />
  </el-card>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const apiKey = ref('')
const datasetId = ref('')
const documentId = ref('')
const segmentText = ref('')
const loading = ref(false)
const result = ref('')
const error = ref('')

const addSegment = async () => {
  result.value = ''
  error.value = ''
  loading.value = true
  try {
    const res = await axios.post('/api/dify/add-segment', {
      apiKey: apiKey.value,
      datasetId: datasetId.value,
      documentId: documentId.value,
      content: segmentText.value
    })
    if (res.data && res.data.data && res.data.data.length > 0 && res.data.data[0].id) {
      result.value = `添加成功，分段ID: ${res.data.data[0].id}`
    } else if (res.data && res.data.error) {
      error.value = '添加失败: ' + res.data.error
    } else {
      error.value = '添加失败，未返回分段ID'
    }
  } catch (e) {
    error.value = '添加失败: ' + (e.response?.data?.error || e.message)
  } finally {
    loading.value = false
  }
}
</script> 