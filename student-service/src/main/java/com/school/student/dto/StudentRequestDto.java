package com.school.student.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequestDTO {

    @NotBlank(message = "Student name is required")
    private String studentName;

    @NotBlank(message = "Student grade is required")
    private String grade;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^\\+?[0-9]{7,15}$",
            message = "Enter valid mobile number"
    )
    private String mobileNumber;

    @NotBlank(message = "School name is required")
    private String schoolName;
}
