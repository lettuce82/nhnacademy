package com.nhnacademy.nhnmart.exception;

public class NoUserExistsException extends RuntimeException {
    public NoUserExistsException() {
        super("No User in login");
    }
}
