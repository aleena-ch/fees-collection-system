package com.school.fee.exception;

public class StudentServiceUnavailableException extends RuntimeException {
    public StudentServiceUnavailableException(String message) {
        super(message);
    }
}
