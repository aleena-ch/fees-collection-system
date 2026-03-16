package com.school.fee.exception;

/**
 * Exception thrown when Student Service
 * is unavailable via Feign fallback.
 * Results in 503 Service Unavailable response.
 */
public class StudentServiceUnavailableException extends RuntimeException {
    public StudentServiceUnavailableException(String message) {
        super(message);
    }
}
