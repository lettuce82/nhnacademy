package com.nhnacademy.nhnmart.controller;

import com.nhnacademy.nhnmart.exception.InquiryNotExistsException;
import com.nhnacademy.nhnmart.exception.NoUserExistsException;
import com.nhnacademy.nhnmart.exception.UserNotExistsException;
import com.nhnacademy.nhnmart.exception.ValidationFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class WebControllerAdvice {
    @ExceptionHandler({NoUserExistsException.class, UserNotExistsException.class, InquiryNotExistsException.class})
    public String handleException(Exception ex, Model model) {
        model.addAttribute("exception", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(ValidationFailedException.class)
    public String handleValidationFailedException(Exception ex, Model model) {
        String errorMessage = ex.getMessage();
        int startIndex = errorMessage.indexOf("Message=") + "Message=".length();
        int endIndex = errorMessage.indexOf(",", startIndex);
        String message = errorMessage.substring(startIndex, endIndex);
        model.addAttribute("exception", message);
        return "error";
    }
}
