package com.nhnacademy.nhnmart.exception;

public class CategoryNotExistsException extends RuntimeException {
    public CategoryNotExistsException() {
        super("Category does not exist");
    }
}
