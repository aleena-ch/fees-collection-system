package com.school.fee.exception;

public class DuplicateFeeException extends RuntimeException {
    public DuplicateFeeException(String message) {
        super(message);
    }
}
