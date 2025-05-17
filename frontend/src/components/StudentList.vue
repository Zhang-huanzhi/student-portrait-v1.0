<template>
  <div class="student-list">
    <div class="top-bar">
      <el-button type="primary" round @click="showAddDialog">
        <el-icon><Plus /></el-icon> 添加学生
      </el-button>
      
      <div class="search-box">
        <el-input
          v-model="searchText"
          placeholder="搜索学生姓名、学号或专业..."
          :prefix-icon="Search"
          clearable
          @input="handleSearch"
        />
      </div>
    </div>
      <el-table 
      :data="filteredStudents" 
      style="width: 100%" 
      header-cell-class-name="table-header"
      :row-class-name="tableRowClassName"
      @row-click="handleRowClick"
      :stripe="true">      <el-table-column prop="studentId" label="学号" sortable width="95">
        <template #default="scope">
          <el-tag size="small">{{ scope.row.studentId }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="姓名" sortable width="110">
        <template #default="scope">
          <div class="name-with-icon">
            <el-avatar :size="22" :icon="User"></el-avatar>
            <span>{{ scope.row.name }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="department" label="院系" min-width="130" />
      <el-table-column prop="major" label="专业" min-width="140" />
      <el-table-column prop="grade" label="年级" width="80" />
      <el-table-column label="操作" width="140">
        <template #default="scope">        <el-button size="small" type="info" @click.stop="handleView(scope.row)" circle>
            <el-icon class="small-icon"><View /></el-icon>
          </el-button>
          <el-button size="small" type="primary" @click.stop="handleEdit(scope.row)" circle>
            <el-icon class="small-icon"><Edit /></el-icon>
          </el-button>
          <el-button size="small" type="danger" @click.stop="handleDelete(scope.row)" circle>
            <el-icon class="small-icon"><Delete /></el-icon>
          </el-button>
        </template>
      </el-table-column>
    </el-table>    <!-- 编辑/添加学生对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle">
      <el-form :model="currentStudent" label-width="80px">
        <el-form-item label="学号">
          <el-input v-model="currentStudent.studentId" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="currentStudent.name" />
        </el-form-item>
        <el-form-item label="院系">
          <el-input v-model="currentStudent.department" />
        </el-form-item>
        <el-form-item label="专业">
          <el-input v-model="currentStudent.major" />
        </el-form-item>
        <el-form-item label="年级">
          <el-input v-model="currentStudent.grade" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </span>
      </template>
    </el-dialog>
      <!-- 学生详情查看对话框 -->
    <el-dialog v-model="detailsVisible" title="学生详情信息" width="65%" custom-class="student-detail-dialog">
      <div class="student-profile-header">
        <div class="student-avatar">
          <el-avatar :size="64" :icon="User"></el-avatar>
        </div>
        <div class="student-basic-info">
          <h2>{{ studentDetail.name }} <el-tag type="success" size="small" effect="dark">在读</el-tag></h2>
          <p class="student-id">学号：{{ studentDetail.studentId }}</p>
        </div>
      </div>
      
      <el-descriptions :column="2" border class="detail-descriptions">
        <el-descriptions-item label="院系">
          <el-tag type="info" effect="plain">{{ studentDetail.department }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="专业">
          <el-tag type="warning" effect="plain">{{ studentDetail.major }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="年级">
          <el-tag type="danger" effect="plain">{{ studentDetail.grade }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="学习进度">
          <el-progress :percentage="studentDetail.studyProgress || 75" :stroke-width="10" :format="formatProgress" />
        </el-descriptions-item>
      </el-descriptions>
      
      <div class="detail-section">
        <h4><el-icon><DataAnalysis /></el-icon> 学习表现</h4>
        <div class="chart-container">
          <div class="gauge-wrapper">
            <el-progress type="dashboard" :percentage="studentDetail.attendanceRate || 92" />
            <div class="gauge-label">出勤率</div>
          </div>
          <div class="gauge-wrapper">
            <el-progress type="dashboard" :percentage="studentDetail.homeworkCompletion || 85" :color="customColors" />
            <div class="gauge-label">作业完成度</div>
          </div>
          <div class="gauge-wrapper">
            <el-progress type="dashboard" :percentage="studentDetail.testScores || 76" :color="customColors" />
            <div class="gauge-label">考试成绩</div>
          </div>
        </div>
      </div>
      
      <div class="detail-section">
        <h4><el-icon><Collection /></el-icon> 个性标签</h4>
        <div class="tags-container">
          <el-tag v-for="(tag, index) in studentTags" :key="index" :type="tagTypes[index % tagTypes.length]" class="student-tag">
            {{ tag }}
          </el-tag>
        </div>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailsVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { studentApi } from '../services/api';
import { ElMessage, ElMessageBox } from 'element-plus';
import { 
  Search, 
  Plus, 
  Edit, 
  Delete, 
  View, 
  DataAnalysis, 
  Collection,
  User
} from '@element-plus/icons-vue';

const students = ref([]);
const dialogVisible = ref(false);
const detailsVisible = ref(false);
const dialogTitle = ref('');
const searchText = ref('');
const customColors = [
  { color: '#f56c6c', percentage: 20 },
  { color: '#e6a23c', percentage: 40 },
  { color: '#5cb87a', percentage: 60 },
  { color: '#1989fa', percentage: 80 },
  { color: '#6f7ad3', percentage: 100 },
];
const tagTypes = ['', 'success', 'warning', 'info', 'danger'];

const currentStudent = ref({
  studentId: '',
  name: '',
  department: '',
  major: '',
  grade: ''
});

const studentDetail = ref({
  studentId: '',
  name: '',
  department: '',
  major: '',
  grade: '',
  studyProgress: 0,
  attendanceRate: 0,
  homeworkCompletion: 0,
  testScores: 0
});

const studentTags = ref([
  '善于思考',
  '编程能力强',
  '团队协作',
  '自主学习',
  '表达能力佳'
]);

// 根据搜索条件过滤学生
const filteredStudents = computed(() => {
  if (!searchText.value) return students.value;
  
  const searchValue = searchText.value.toLowerCase();
  return students.value.filter(student => {
    return (
      (student.studentId && student.studentId.toLowerCase().includes(searchValue)) ||
      (student.name && student.name.toLowerCase().includes(searchValue)) ||
      (student.department && student.department.toLowerCase().includes(searchValue)) ||
      (student.major && student.major.toLowerCase().includes(searchValue)) ||
      (student.grade && student.grade.toLowerCase().includes(searchValue))
    );
  });
});

const fetchStudents = async () => {
  try {
    const response = await studentApi.getAllStudents();
    students.value = response.data;
  } catch (error) {
    ElMessage.error('获取学生列表失败');
  }
};

const showAddDialog = () => {
  currentStudent.value = {
    studentId: '',
    name: '',
    department: '',
    major: '',
    grade: ''
  };
  dialogTitle.value = '添加学生';
  dialogVisible.value = true;
};

const handleEdit = (student) => {
  currentStudent.value = { ...student };
  dialogTitle.value = '编辑学生';
  dialogVisible.value = true;
};

const handleView = (student) => {
  // 设置学生详情，这里可以从后端获取更多详细信息
  // 目前使用模拟数据
  studentDetail.value = {
    ...student,
    studyProgress: Math.floor(Math.random() * 30) + 70, // 70-100
    attendanceRate: Math.floor(Math.random() * 20) + 80, // 80-100
    homeworkCompletion: Math.floor(Math.random() * 25) + 75, // 75-100
    testScores: Math.floor(Math.random() * 40) + 60 // 60-100
  };
  
  // 随机生成不同的标签
  const allTags = ['善于思考', '编程能力强', '团队协作', '自主学习', '表达能力佳', 
                  '需要辅导', '创新思维', '领导能力', '细心严谨', '学习积极性高'];
  
  studentTags.value = [];
  const tagCount = Math.floor(Math.random() * 3) + 3; // 3-5个标签
  
  const usedIndices = new Set();
  while(studentTags.value.length < tagCount) {
    const index = Math.floor(Math.random() * allTags.length);
    if(!usedIndices.has(index)) {
      studentTags.value.push(allTags[index]);
      usedIndices.add(index);
    }
  }
  
  detailsVisible.value = true;
};

const handleSearch = () => {
  // 搜索框内容变化时触发过滤
};

const tableRowClassName = ({ row }) => {
  return 'student-table-row';
};

const handleRowClick = (row) => {
  handleView(row);
};

const formatProgress = (percentage) => {
  return percentage < 70 ? `需要提升 ${percentage}%` : `良好 ${percentage}%`;
};

const handleDelete = async (student) => {
  try {
    await ElMessageBox.confirm('确定要删除该学生吗？', '提示', {
      type: 'warning'
    });
    await studentApi.deleteStudent(student.id);
    ElMessage.success('删除成功');
    fetchStudents();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败');
    }
  }
};

const handleSave = async () => {
  try {
    if (currentStudent.value.id) {
      await studentApi.updateStudent(currentStudent.value.id, currentStudent.value);
      ElMessage.success('更新成功');
    } else {
      await studentApi.createStudent(currentStudent.value);
      ElMessage.success('添加成功');
    }
    dialogVisible.value = false;
    fetchStudents();
  } catch (error) {
    ElMessage.error('保存失败');
  }
};

onMounted(() => {
  fetchStudents();
});
</script>

<style scoped>
.student-list {
  padding: 14px;
}

.top-bar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
  align-items: center;
  background: #f0f7ff;
  padding: 14px 16px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.top-bar:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.search-box {
  width: 320px;
  transition: all 0.3s ease;
}

.search-box:focus-within {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.table-header {
  background: linear-gradient(90deg, #409eff 0%, #66b1ff 100%);
  color: #fff;
  font-weight: bold;
  font-size: 16px;
  padding: 12px 8px;
}

.el-button {
  margin-right: 4px;
  transition: all 0.3s ease;
  padding: 6px;
}

.el-button:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.small-icon {
  font-size: 12px;
}

.el-table {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
  font-size: 13px;
}

.el-table :deep(.el-table__row) {
  height: 50px;
}

.el-table :deep(.cell) {
  padding: 0 8px;
  line-height: 1.4;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.detail-section {
  margin-top: 24px;
  padding: 16px;
  background-color: #f9fafc;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  border-left: 4px solid #409eff;
}

.detail-section:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
}

.detail-section h4 {
  margin-top: 0;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  color: #303133;
  font-size: 18px;
}

.detail-section h4 .el-icon {
  margin-right: 8px;
  color: #409eff;
  font-size: 20px;
  background: #e6f1fc;
  padding: 6px;
  border-radius: 50%;
}

.chart-container {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  margin: 10px 0;
}

.gauge-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 16px;
  padding: 16px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.gauge-wrapper:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.gauge-label {
  margin-top: 12px;
  font-weight: bold;
  color: #303133;
  font-size: 15px;
  text-align: center;
  padding: 6px 12px;
  background: #f0f7ff;
  border-radius: 20px;
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  padding: 10px;
  background: white;
  border-radius: 12px;
}

.student-tag {
  padding: 8px 16px;
  font-size: 14px;
  border-radius: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.student-tag:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.student-table-row {
  cursor: pointer;
  transition: all 0.2s ease;
}

.student-table-row:hover {
  background-color: #f0f7ff !important;
}

.name-with-icon {
  display: flex;
  align-items: center;
  gap: 6px;
}

/* 为表格添加一些过渡效果 */
.el-table .cell {
  transition: all 0.2s ease;
}

/* 优化表格布局 */
.el-table :deep(th.el-table__cell) {
  padding: 8px 0;
  background-color: #f2f6fc;
}

.el-table :deep(td.el-table__cell) {
  padding: 4px 0;
}

/* 学生详情对话框样式 */
.student-detail-dialog {
  border-radius: 16px;
  overflow: hidden;
}

:deep(.el-dialog__header) {
  background: linear-gradient(90deg, #409eff 0%, #66b1ff 100%);
  color: white;
  padding: 15px 20px;
}

:deep(.el-dialog__title) {
  color: white;
  font-weight: bold;
  font-size: 18px;
}

:deep(.el-dialog__headerbtn .el-dialog__close) {
  color: white;
}

.student-profile-header {
  display: flex;
  align-items: center;
  padding: 16px;
  background: #f5f7fa;
  margin-bottom: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.student-avatar {
  margin-right: 20px;
}

.student-basic-info h2 {
  margin: 0 0 8px 0;
  font-size: 22px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.student-id {
  color: #606266;
  margin: 0;
}

.detail-descriptions {
  margin-bottom: 20px;
}
</style>