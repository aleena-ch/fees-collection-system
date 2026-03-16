package com.school.fee.exception;

/**
 * Exception thrown when a duplicate fee payment
 * is detected for the same student, fee type,
 * month and academic year combination.
 * Results in 409 Conflict response.
 */
public class DuplicateFeeException extends RuntimeException {
    public DuplicateFeeException(String message) {
        super(message);
    }
}
