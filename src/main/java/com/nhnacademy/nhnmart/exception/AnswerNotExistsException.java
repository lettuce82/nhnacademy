package com.nhnacademy.nhnmart.exception;

public class AnswerNotExistsException extends RuntimeException {
    public AnswerNotExistsException() {
        super("Answer does not exist");
    }
}
