package com.nhnacademy.springmvc1.exception;

public class StudentAlreadyExistsException extends RuntimeException {
    public StudentAlreadyExistsException(String id) {
        super("Student with id " + id + " already exists");
    }
}
