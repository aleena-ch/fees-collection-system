package com.school.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
