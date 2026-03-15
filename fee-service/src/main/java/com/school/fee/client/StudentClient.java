package com.school.fee.client;

import com.school.fee.dto.StudentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "student-service", url = "${services.student-service.url}", fallback = StudentClientFallback.class)
public interface StudentClient {

    @GetMapping("/api/v1/students/{studentId}")
    StudentResponseDTO getStudent(
            @PathVariable String studentId);
}

