package com.nhnacademy.springmvc1.controller;

import com.nhnacademy.springmvc1.domain.Student;
import com.nhnacademy.springmvc1.domain.StudentRegisterRequest;
import com.nhnacademy.springmvc1.exception.ValidationFailedException;
import com.nhnacademy.springmvc1.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/student/register")
public class StudentRegisterController {
    private final StudentRepository studentRepository;

    public StudentRegisterController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public String studentRegisterForm() {
        return "studentRegister";
    }

    @PostMapping
    public ModelAndView RegisterStudent(@Valid @ModelAttribute StudentRegisterRequest studentRegisterRequest,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        Student student = studentRepository.register(
                studentRegisterRequest.getId(),
                studentRegisterRequest.getPwd(),
                studentRegisterRequest.getName(),
                studentRegisterRequest.getEmail(),
                studentRegisterRequest.getScore(),
                studentRegisterRequest.getRating()
        );

        ModelAndView mav = new ModelAndView("studentRegister");
        mav.addObject("student", Student.constructPasswordMaskedUser(student));

        return mav;
    }

}