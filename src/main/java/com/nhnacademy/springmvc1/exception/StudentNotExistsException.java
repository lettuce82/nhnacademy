package com.nhnacademy.springmvc1.exception;

public class StudentNotExistsException extends RuntimeException {
    public StudentNotExistsException(String id) {

        super("student not exists(id:{" + id + "})");
    }
}
