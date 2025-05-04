import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

export const studentApi = {
  getAllStudents() {
    return api.get('/students');
  },
  getStudentById(id) {
    return api.get(`/students/${id}`);
  },
  getStudentByStudentId(studentId) {
    return api.get(`/students/byStudentId/${studentId}`);
  },
  createStudent(student) {
    return api.post('/students', student);
  },
  updateStudent(id, student) {
    return api.put(`/students/${id}`, student);
  },
  deleteStudent(id) {
    return api.delete(`/students/${id}`);
  }
};

export const chatApi = {
  sendMessage(message, userId) {
    return api.post('/chat', { query: message, user: userId });
  }
}; 