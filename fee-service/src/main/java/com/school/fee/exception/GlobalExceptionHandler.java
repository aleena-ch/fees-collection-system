package com.school.fee.exception;

import com.school.fee.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFeeNotFoundException(FeeNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.buildError(
                        exception.getMessage(),
                        "FEE_NOT_FOUND",
                        404));
    }

    @ExceptionHandler(DuplicateFeeException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateFeeException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse.buildError(
                        exception.getMessage(),
                        "DUPLICATE_FEE",
                        409));
    }

    @ExceptionHandler(StudentServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleServiceUnavailableException(StudentServiceUnavailableException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ErrorResponse.buildError(
                        ex.getMessage(),
                        "SERVICE_UNAVAILABLE",
                        503));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField()
                        + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.buildError(
                        "Invalid input: " + message,
                        "VALIDATION_ERROR",
                        400));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.buildError(
                        "An unexpected error occurred. " +
                                "Please try again later",
                        "INTERNAL_SERVER_ERROR",
                        500));
    }
}
