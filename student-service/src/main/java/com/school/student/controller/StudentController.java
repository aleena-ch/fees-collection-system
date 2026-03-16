package com.school.student.controller;

import com.school.student.dto.StudentRequestDTO;
import com.school.student.dto.StudentResponseDTO;
import com.school.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing student operations.
 */
@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService service;

    /**
     * Adds a new student to the system.
     *
     * @param request Student details
     * @return 201 Created with student details
     */
    @PostMapping
    public ResponseEntity<StudentResponseDTO> addStudent(@Valid @RequestBody StudentRequestDTO request) {

        log.info("Request to add student: {}", request.getStudentName());
        StudentResponseDTO responseDTO = service.addStudent(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    /**
     * Retrieves all students with pagination.
     *
     * @param pageNumber Page number (default 0)
     * @param size       Page size (default 10)
     * @return 200 OK with page of students
     */
    @GetMapping
    public ResponseEntity<Page<StudentResponseDTO>> getAllStudents(
            @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int size) {
        log.info("Request to get all student details");
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<StudentResponseDTO> response = service.getAllStudentsDetails(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a student by student ID.
     *
     * @param studentId Unique student identifier
     * @return 200 OK with student details
     */
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponseDTO> getStudent(@PathVariable String studentId) {
        log.info("Request to get student details with Id: {}", studentId);
        StudentResponseDTO responseDTO = service.getStudentById(studentId);
        return ResponseEntity.ok(responseDTO);
    }
}
