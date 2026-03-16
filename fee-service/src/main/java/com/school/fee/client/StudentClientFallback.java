package com.school.fee.client;

import com.school.fee.dto.StudentResponseDTO;
import com.school.fee.exception.StudentServiceUnavailableException;
import org.springframework.stereotype.Component;

/**
 * Fallback implementation for StudentClient.
 * Triggered when Student Service is unavailable
 * or circuit breaker is open.
 */
@Component
public class StudentClientFallback implements StudentClient {

    /**
     * Fallback method when Student Service
     * is unavailable.
     * Throws StudentServiceUnavailableException
     * which is handled by GlobalExceptionHandler
     * returning 503 Service Unavailable.
     *
     * @param studentId Student identifier
     * @throws StudentServiceUnavailableException always thrown as fallback
     */
    @Override
    public StudentResponseDTO getStudent(
            String studentId) {
        throw new StudentServiceUnavailableException(
                "Student service is currently unavailable. Please try again later");
    }
}
