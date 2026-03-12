package com.school.student.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "students")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id",unique = true,nullable = false)
    private int studentId;

    @Column(name = "student_name",nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String grade;

    @Column(name = "mobile_number",nullable = false,length = 15)
    private String mobileNumber;

    @Column(name = "school_name",nullable = false)
    private String SchoolName;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
