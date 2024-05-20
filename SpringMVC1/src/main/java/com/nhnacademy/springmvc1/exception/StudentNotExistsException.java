package com.nhnacademy.springmvc1.exception;

public class StudentNotExistsException extends RuntimeException {
    public StudentNotExistsException(String message) {
        super(message);
    }
}
