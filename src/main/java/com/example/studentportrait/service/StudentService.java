package com.example.studentportrait.service;

import com.example.studentportrait.entity.Student;
import com.example.studentportrait.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DifyKbService difyKbService;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId);
    }

    public Student createStudent(Student student) {
        // 先保存到MySQL
        Student savedStudent = studentRepository.save(student);
        // 创建Dify文档
        String difyDocId = difyKbService.createDocumentInDify(savedStudent);
        if (difyDocId != null) {
            savedStudent.setDifyDocumentId(difyDocId);
            // 再次保存以持久化difyDocumentId
            savedStudent = studentRepository.save(savedStudent);
        }
        return savedStudent;
    }

    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        student.setStudentId(studentDetails.getStudentId());
        student.setName(studentDetails.getName());
        student.setDepartment(studentDetails.getDepartment());
        student.setMajor(studentDetails.getMajor());
        student.setGrade(studentDetails.getGrade());
        Student updatedStudent = studentRepository.save(student);
        // 同步更新Dify文档
        difyKbService.updateDocumentInDify(updatedStudent);
        return updatedStudent;
    }

    public void deleteStudent(Long id) {
        // 先查出difyDocumentId
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isPresent()) {
            String difyDocId = studentOpt.get().getDifyDocumentId();
            if (difyDocId != null && !difyDocId.isEmpty()) {
                difyKbService.deleteDocumentFromDify(difyDocId);
            }
        }
        // 再删除MySQL记录
        studentRepository.deleteById(id);
    }
} 