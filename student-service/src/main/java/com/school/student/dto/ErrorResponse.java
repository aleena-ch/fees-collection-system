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
public class ErrorResponse {

    private boolean success;
    private String message;
    private String errorCode;
    private int status;
    private LocalDateTime timestamp;

    public static ErrorResponse buildError(String message, String errorCode, int status) {

        return ErrorResponse.builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .status(status)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
