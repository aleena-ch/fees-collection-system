package com.school.student;

import com.school.student.dto.StudentRequestDTO;
import com.school.student.dto.StudentResponseDTO;
import com.school.student.model.Student;
import net.datafaker.Faker;

import java.time.LocalDateTime;

public class StudentTestDataBuilder {

    private static final Faker faker = new Faker();

    public static StudentRequestDTO buildRequest() {
        return StudentRequestDTO.builder()
                .studentName(faker.name().fullName())
                .grade(faker.number()
                        .numberBetween(1, 12) + "-A")
                .mobileNumber("+9715" +
                        faker.number().digits(8))
                .schoolName(faker.university().name())
                .build();
    }

    public static Student buildStudent(
            String studentId) {
        return Student.builder()
                .studentId(studentId)
                .studentName(faker.name().fullName())
                .grade(faker.number()
                        .numberBetween(1, 12) + "-A")
                .mobileNumber("+9715" +
                        faker.number().digits(8))
                .schoolName(faker.university().name())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Student buildStudentFromRequest(
            String studentId,
            StudentRequestDTO request) {
        return Student.builder()
                .studentId(studentId)
                .studentName(request.getStudentName())
                .grade(request.getGrade())
                .mobileNumber(request.getMobileNumber())
                .schoolName(request.getSchoolName())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static StudentResponseDTO buildResponse(
            Student student) {
        return StudentResponseDTO.builder()
                .studentId(student.getStudentId())
                .studentName(student.getStudentName())
                .grade(student.getGrade())
                .mobileNumber(student.getMobileNumber())
                .schoolName(student.getSchoolName())
                .createdAt(student.getCreatedAt())
                .build();
    }
}
