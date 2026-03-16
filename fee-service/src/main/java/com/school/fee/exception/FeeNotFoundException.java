package com.school.fee.exception;

/**
 * Exception thrown when a fee receipt
 * is not found in the system.
 * Results in 404 Not Found response.
 */
public class FeeNotFoundException extends RuntimeException {
    public FeeNotFoundException(String message) {
        super(message);
    }
}
