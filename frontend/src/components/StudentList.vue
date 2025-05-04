<template>
  <div class="student-list">
    <el-button type="primary" icon="el-icon-plus" round @click="showAddDialog">添加学生</el-button>
    
    <el-table :data="students" style="width: 100%" header-cell-class-name="table-header">
      <el-table-column prop="studentId" label="学号" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="department" label="院系" />
      <el-table-column prop="major" label="专业" />
      <el-table-column prop="grade" label="年级" />
      <el-table-column label="操作" width="160">
        <template #default="scope">
          <el-button size="small" icon="el-icon-edit" round @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" icon="el-icon-delete" round @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { studentApi } from '../services/api';
import { ElMessage, ElMessageBox } from 'element-plus';
import 'element-plus/es/components/icon/style/css';

const students = ref([]);
const dialogVisible = ref(false);
const dialogTitle = ref('');
const currentStudent = ref({
  studentId: '',
  name: '',
  department: '',
  major: '',
  grade: ''
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
  padding: 20px;
}
.table-header {
  background: linear-gradient(90deg, #409eff 0%, #66b1ff 100%);
  color: #fff;
  font-weight: bold;
  font-size: 16px;
}
.el-button {
  margin-right: 8px;
}
</style> 