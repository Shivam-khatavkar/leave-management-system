package com.company.lms.exception;

public class InvalidLeaveDateException
        extends RuntimeException {

    public InvalidLeaveDateException(String message) {
        super(message);
    }
}