package com.school.student;

import com.school.student.dto.StudentRequestDTO;
import com.school.student.dto.StudentResponseDTO;
import com.school.student.exception.StudentNotFoundException;
import com.school.student.model.Student;
import com.school.student.repository.StudentRepository;
import com.school.student.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository repository;
    @InjectMocks
    private StudentService studentService;
    private StudentRequestDTO requestDTO;
    private Student student;

    @BeforeEach
    void setUp() {

        requestDTO = StudentTestDataBuilder.buildRequest();
        student = StudentTestDataBuilder
                .buildStudentFromRequest(
                        "STU-2026-001", requestDTO);
    }

    @Test
    void addStudent_ShouldReturnResponseDTO_WhenValidRequest() {

        when(repository.count()).thenReturn(0L);
        when(repository.save(any(Student.class)))
                .thenReturn(student);

        StudentResponseDTO response =
                studentService.addStudent(requestDTO);

        assertNotNull(response);
        assertEquals(requestDTO.getStudentName(), response.getStudentName());
        assertEquals(requestDTO.getGrade(), response.getGrade());
        assertEquals(requestDTO.getMobileNumber(), response.getMobileNumber());
        assertEquals(requestDTO.getSchoolName(), response.getSchoolName());
        assertTrue(response.getStudentId().startsWith("STU-"));

        verify(repository, times(1))
                .save(any(Student.class));
    }

    @Test
    void addStudent_ShouldGenerateCorrectStudentId() {

        when(repository.count()).thenReturn(4L);
        when(repository.save(any(Student.class))).thenReturn(student);

        studentService.addStudent(requestDTO);

        verify(repository, times(1))
                .save(argThat(s ->
                        s.getStudentId()
                                .contains("STU-")));
    }

    @Test
    void getStudentById_ShouldReturnStudent_WhenExists() {

        when(repository.findByStudentId("STU-2026-001"))
                .thenReturn(Optional.of(student));

        StudentResponseDTO response = studentService.getStudentById("STU-2026-001");

        assertEquals(requestDTO.getStudentName(), response.getStudentName());
        assertEquals(requestDTO.getGrade(), response.getGrade());
        assertEquals(requestDTO.getSchoolName(), response.getSchoolName());
        assertTrue(response.getStudentId().startsWith("STU-"));
        verify(repository, times(1))
                .findByStudentId("STU-2026-001");
    }

    @Test
    void getStudentById_ShouldThrowException_WhenNotFound() {

        when(repository.findByStudentId("STU-9999-999"))
                .thenReturn(Optional.empty());

        StudentNotFoundException exception =
                assertThrows(
                        StudentNotFoundException.class,
                        () -> studentService
                                .getStudentById("STU-9999-999"));

        assertEquals(
                "Student not found with id : STU-9999-999",
                exception.getMessage());
    }

    @Test
    void getAllStudents_ShouldReturnPageOfStudents() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Student> studentPage = new PageImpl<>(List.of(student));
        when(repository.findAll(pageable)).thenReturn(studentPage);

        Page<StudentResponseDTO> response = studentService.getAllStudentsDetails(pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(requestDTO.getStudentName(), response.getContent()
                .get(0).getStudentName());
    }

    @Test
    void getAllStudents_ShouldReturnEmptyPage_WhenNoStudents() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Student> emptyPage = new PageImpl<>(List.of());
        when(repository.findAll(pageable)).thenReturn(emptyPage);

        Page<StudentResponseDTO> response = studentService.getAllStudentsDetails(pageable);

        assertNotNull(response);
        assertEquals(0, response.getTotalElements());
        assertTrue(response.getContent().isEmpty());
    }
}
