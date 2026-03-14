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

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService service;

    @PostMapping
    private ResponseEntity<StudentResponseDTO> addStudent(@Valid @RequestBody StudentRequestDTO request) {

        log.info("Request to add student: {}", request.getStudentName());
        StudentResponseDTO responseDTO = service.addStudent(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    @GetMapping
    private ResponseEntity<Page<StudentResponseDTO>> getAllStudents(
            @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int size) {
        log.info("Request to get all student details");
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<StudentResponseDTO> response = service.getAllStudentsDetails(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{studentId}")
    private ResponseEntity<StudentResponseDTO> getStudent(@PathVariable String studentId) {
        log.info("Request to get student details with Id: {}", studentId);
        StudentResponseDTO responseDTO = service.getStudentById(studentId);
        return ResponseEntity.ok(responseDTO);
    }
}
