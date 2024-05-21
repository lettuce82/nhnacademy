package com.nhnacademy.springmvc1.controller;

import com.nhnacademy.springmvc1.exception.NoUsertExistsException;
import com.nhnacademy.springmvc1.exception.StudentAlreadyExistsException;
import com.nhnacademy.springmvc1.exception.StudentNotExistsException;
import com.nhnacademy.springmvc1.exception.ValidationFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class WebControllerAdvice {
    @ExceptionHandler({NoUsertExistsException.class, StudentAlreadyExistsException.class
    , StudentNotExistsException.class})
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
