package com.school.fee.client;

import com.school.fee.dto.StudentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for communicating with Student Service.
 * Provides methods to retrieve student information.
 * Falls back to StudentClientFallback when
 * Student Service is unavailable.
 */
@FeignClient(name = "student-service",
        url = "${services.student-service.url}",
        fallback = StudentClientFallback.class)
public interface StudentClient {

    /**
     * Retrieves a student by their unique student ID
     * from Student Service via HTTP GET.
     *
     * @param studentId Unique student identifier
     * @return StudentResponseDTO with student details
     */
    @GetMapping("/api/v1/students/{studentId}")
    StudentResponseDTO getStudent(@PathVariable String studentId);
}

