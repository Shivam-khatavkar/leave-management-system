package com.company.lms.exception;

public class LeaveOverlapException
        extends RuntimeException {

    public LeaveOverlapException(String message) {
        super(message);
    }
}