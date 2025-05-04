package com.example.studentportrait.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String studentId;

    private String name;
    private String department;
    private String major;
    private String grade;

    // 新增字段：Dify文档ID
    private String difyDocumentId;
} 