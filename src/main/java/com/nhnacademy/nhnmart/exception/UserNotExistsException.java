package com.nhnacademy.nhnmart.exception;

public class UserNotExistsException extends RuntimeException {
    public UserNotExistsException(String id) {
        super("User with id " + id + " does not exist");
    }
}
