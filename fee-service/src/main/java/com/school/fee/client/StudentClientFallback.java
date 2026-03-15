package com.school.fee.client;

import com.school.fee.dto.StudentResponseDTO;
import com.school.fee.exception.StudentServiceUnavailableException;
import org.springframework.stereotype.Component;

@Component
public class StudentClientFallback implements StudentClient {
    @Override
    public StudentResponseDTO getStudent(
            String studentId) {
        throw new StudentServiceUnavailableException(
                "Student service is currently unavailable. Please try again later");
    }
}
