package com.school.fee.exception;

import com.school.fee.dto.ErrorResponse;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.stream.Collectors;

/**
 * Global exception handler for Fee Service.
 * Intercepts all exceptions thrown across
 * the application and returns consistent
 * error responses to the client.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Handles FeeNotFoundException.
     * Thrown when a receipt is not found.
     *
     * @param exception FeeNotFoundException
     * @return 404 Not Found with error details
     */
    @ExceptionHandler(FeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFeeNotFoundException(
            FeeNotFoundException exception) {

        log.error("Fee not found: {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.buildError(
                        exception.getMessage(),
                        "FEE_NOT_FOUND",
                        404));
    }

    /**
     * Handles DuplicateFeeException.
     * Thrown when same fee is paid twice
     * for same month and academic year.
     *
     * @param exception DuplicateFeeException
     * @return 409 Conflict with error details
     */
    @ExceptionHandler(DuplicateFeeException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateException(
            DuplicateFeeException exception) {

        log.error("Duplicate fee: {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse.buildError(
                        exception.getMessage(),
                        "DUPLICATE_FEE",
                        409));
    }

    /**
     * Handles StudentServiceUnavailableException.
     * Thrown by Feign fallback when
     * Student Service is unreachable.
     *
     * @param ex StudentServiceUnavailableException
     * @return 503 Service Unavailable
     */
    @ExceptionHandler(StudentServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleServiceUnavailableException(
            StudentServiceUnavailableException ex) {

        log.error("Student service unavailable: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ErrorResponse.buildError(
                        ex.getMessage(),
                        "SERVICE_UNAVAILABLE",
                        503));
    }

    /**
     * Handles FeignException.NotFound.
     * Thrown when Student Service returns 404.
     * Extracts clean message from Feign response.
     *
     * @param ex FeignException.NotFound
     * @return 404 Not Found with student message
     */
    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<ErrorResponse> handleFeignNotFoundException(FeignException.NotFound ex) {

        String message = "Student not found";
        try {
            String responseBody = ex.contentUTF8();
            JsonNode json = objectMapper.readTree(responseBody);

            if (json.has("message")) {
                message = json.path("message")
                        .asText("Student not found");
            }
        } catch (Exception e) {
            log.warn("Could not parse Feign error: {}", e.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.buildError(
                        message,
                        "STUDENT_NOT_FOUND",
                        HttpStatus.NOT_FOUND.value()));
    }

    /**
     * Handles all other FeignExceptions.
     * Thrown when Student Service returns
     * non-404 error or is unreachable.
     * Must be declared AFTER
     * FeignException.NotFound handler.
     *
     * @param ex FeignException
     * @return 503 Service Unavailable
     */
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignError(FeignException ex) {

        log.error("Feign error: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ErrorResponse.buildError(
                        "Student service is currently unavailable. Please try again later",
                        "SERVICE_UNAVAILABLE",
                        HttpStatus.SERVICE_UNAVAILABLE.value()));
    }

    /**
     * Handles MethodArgumentNotValidException.
     * Thrown when request body fails
     * Bean Validation constraints.
     * Collects all field errors into
     * single message.
     *
     * @param exception validation exception
     * @return 400 Bad Request with field errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException exception) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField()
                        + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.error("Validation failed: {}", message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.buildError(
                        "Invalid input: " + message,
                        "VALIDATION_ERROR",
                        HttpStatus.NOT_FOUND.value()));
    }

    /**
     * Handles all unexpected exceptions.
     * Catch-all handler that must be
     * declared last.
     * Logs full stack trace for debugging.
     *
     * @param exception any unexpected exception
     * @return 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception exception) {
        log.error("Unexpected error: {}", exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.buildError(
                        "An unexpected error occurred. " +
                                "Please try again later",
                        "INTERNAL_SERVER_ERROR",
                        HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
