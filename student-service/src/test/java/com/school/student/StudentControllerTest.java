package com.school.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.student.controller.StudentController;
import com.school.student.dto.StudentRequestDTO;
import com.school.student.dto.StudentResponseDTO;
import com.school.student.exception.GlobalExceptionHandler;
import com.school.student.exception.StudentNotFoundException;
import com.school.student.model.Student;
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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private StudentRequestDTO requestDTO;
    private StudentResponseDTO responseDTO;
    private Student student;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(studentController)
                .setControllerAdvice(
                        new GlobalExceptionHandler())
                .setCustomArgumentResolvers(
                        new PageableHandlerMethodArgumentResolver())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        requestDTO = StudentTestDataBuilder
                .buildRequest();

        student = StudentTestDataBuilder
                .buildStudentFromRequest(
                        "STU-2026-001", requestDTO);

        responseDTO = StudentTestDataBuilder
                .buildResponse(student);
    }

    @Test
    void addStudent_ShouldReturn201_WhenValidRequest()
            throws Exception {
        when(studentService.addStudent(
                any(StudentRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId")
                        .value("STU-2026-001"))
                .andExpect(jsonPath("$.studentName")
                        .value(requestDTO.getStudentName()))
                .andExpect(jsonPath("$.grade")
                        .value(requestDTO.getGrade()))
                .andExpect(jsonPath("$.mobileNumber")
                        .value(requestDTO.getMobileNumber()))
                .andExpect(jsonPath("$.schoolName")
                        .value(requestDTO.getSchoolName()));

        verify(studentService, times(1))
                .addStudent(any(StudentRequestDTO.class));
    }

    @Test
    void addStudent_ShouldReturn400_WhenInvalidRequest()
            throws Exception {

        StudentRequestDTO invalidRequest =
                StudentRequestDTO.builder()
                        .studentName("")
                        .grade("")
                        .mobileNumber("123")
                        .schoolName("")
                        .build();

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(studentService, never()).addStudent(any());
    }

    @Test
    void getStudent_ShouldReturn200_WhenExists()
            throws Exception {
        when(studentService.getStudentById(
                "STU-2026-001"))
                .thenReturn(responseDTO);

        mockMvc.perform(get(
                        "/api/v1/students/STU-2026-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId")
                        .value("STU-2026-001"))
                .andExpect(jsonPath("$.studentName")
                        .value(requestDTO.getStudentName()));

        verify(studentService, times(1))
                .getStudentById("STU-2026-001");
    }

    @Test
    void getStudent_ShouldReturn404_WhenNotFound()
            throws Exception {
        when(studentService.getStudentById(
                "STU-9999-999"))
                .thenThrow(new StudentNotFoundException(
                        "Student not found with id : " +
                                "STU-9999-999"));

        mockMvc.perform(get(
                        "/api/v1/students/STU-9999-999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Student not found " +
                                "with id : STU-9999-999"));

        verify(studentService, times(1))
                .getStudentById("STU-9999-999");
    }

    @Test
    void getAllStudents_ShouldReturn200_WithPage()
            throws Exception {
        Page<StudentResponseDTO> page = new PageImpl<>(
                List.of(responseDTO),
                PageRequest.of(0, 10),
                1);

        when(studentService.getAllStudentsDetails(
                any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/students")
                        .param("pageNumber", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(studentService, times(1))
                .getAllStudentsDetails(any());
    }
}
