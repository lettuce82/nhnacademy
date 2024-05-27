package com.nhnacademy.nhnmart.exception;

public class InquiryNotExistsException extends RuntimeException {
    public InquiryNotExistsException() {
        super("Inquiry does not exist");
    }
}
