package com.school.student.service;

import com.school.student.dto.StudentRequestDTO;
import com.school.student.dto.StudentResponseDTO;
import com.school.student.exception.StudentNotFoundException;
import com.school.student.model.Student;
import com.school.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Service class for managing student operations.
 * Handles creation and retrieval of student records.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final static String STUDENT_ID_PREFIX = "STU";
    private final StudentRepository repository;

    /**
     * Adds a new student to the system.
     * Generates unique student ID using DB sequence.
     *
     * @param studentRequest Student details from request
     * @return StudentResponseDTO with generated studentId
     */
    @Transactional
    public StudentResponseDTO addStudent(StudentRequestDTO studentRequest) {

        log.info("Adding student: {}", studentRequest.getStudentName());

        Student student = Student.builder()
                .studentName(studentRequest.getStudentName())
                .grade(studentRequest.getGrade())
                .mobileNumber(studentRequest.getMobileNumber())
                .schoolName(studentRequest.getSchoolName()).build();

        Student savedStudent = repository.save(student);

        savedStudent.setStudentId(String.format("%s-%d-%03d", STUDENT_ID_PREFIX,
                LocalDate.now().getYear(),
                savedStudent.getId()));

        Student updated = repository.save(savedStudent);
        log.info("Saved student with ID : {}", updated.getStudentId());
        return toResponseDTO(updated);
    }

    /**
     * Retrieves all students with pagination.
     *
     * @param pageable Pagination parameters
     * @return Page of StudentResponseDTO
     */
    @Transactional(readOnly = true)
    public Page<StudentResponseDTO> getAllStudentsDetails(Pageable pageable) {
        Page<Student> students = repository.findAll(pageable);
        return students.map(this::toResponseDTO);
    }

    /**
     * Retrieves a student by their unique student ID.
     *
     * @param studentId Unique student identifier
     * @return StudentResponseDTO if found
     * @throws StudentNotFoundException if not found
     */
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(String studentId) {
        Student student = repository.findByStudentId(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id : " + studentId));
        return toResponseDTO(student);
    }

    private StudentResponseDTO toResponseDTO(Student student) {
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
