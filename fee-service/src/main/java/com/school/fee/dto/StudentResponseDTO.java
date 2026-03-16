package com.school.fee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for receiving student data from
 * Student Service via Feign Client.
 * Fields must match Student Service
 * StudentResponseDTO exactly.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDTO {

    private String studentId;
    private String studentName;
    private String grade;
    private String mobileNumber;
    private String schoolName;
    private LocalDateTime createdAt;

}

