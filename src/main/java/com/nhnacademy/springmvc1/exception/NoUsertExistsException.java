package com.nhnacademy.springmvc1.exception;

public class NoUsertExistsException extends RuntimeException {
    public NoUsertExistsException() {
        super("No User in login");
    }
}
