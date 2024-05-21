package com.nhnacademy.springmvc1.controller;

import com.nhnacademy.springmvc1.domain.Student;
import com.nhnacademy.springmvc1.domain.StudentModifyRequest;
import com.nhnacademy.springmvc1.exception.NoUsertExistsException;
import com.nhnacademy.springmvc1.repository.StudentRepository;
import com.nhnacademy.springmvc1.validator.StudentModifyRequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequestMapping("/student")
public class StudentController {
    private final StudentRepository studentRepository;
    private final StudentModifyRequestValidator validator;

    public StudentController(StudentRepository studentRepository, StudentModifyRequestValidator studentModifyRequestValidator) {
        this.studentRepository = studentRepository;
        this.validator = studentModifyRequestValidator;
    }

    @GetMapping(value = "/{id}")
    public String viewStudent(@PathVariable("id") String id, @ModelAttribute("student") Student student, Model model) {
        if (student == null || StringUtils.isEmpty(id) || studentRepository.getStudent(id) == null) {
            throw new NoUsertExistsException();
        }

        model.addAttribute("student", student);

        return "studentView";
    }

    @GetMapping(value = "/{id}", params = "hideScore")
    public String viewStudentWithoutScore(@PathVariable("id") String id, @ModelAttribute("student") Student student, Model model) {
        if (student == null || StringUtils.isEmpty(id) || studentRepository.getStudent(id) == null) {
            throw new NoUsertExistsException();
        }

        model.addAttribute("isHide", true);
        model.addAttribute("student", student);
        return "studentView";
    }

    @ModelAttribute("student")
    public Student student(@CookieValue(value = "SESSION", required = false) String sessionId) {
        if (!StringUtils.hasText(sessionId)) {
            throw new NoUsertExistsException();
        } else {
            return studentRepository.getStudent(sessionId);
        }
    }

    @GetMapping("/{id}/modify")
    public String studentModifyForm(Model model, @PathVariable("id") String id,
                                    @ModelAttribute("student") Student student) {
        if (student == null || StringUtils.isEmpty(id) || studentRepository.getStudent(id) == null) {
            throw new NoUsertExistsException();
        }

        model.addAttribute("student", student);
        return "studentModify";
    }

    @PostMapping("/{id}/modify")
    public String modifyStudent(@ModelAttribute("student") Student student,
                                @Validated @ModelAttribute StudentModifyRequest studentModifyRequest,
                                BindingResult bindingResult) {

        if (bindingResult.hasErrors() || student == null) {
            throw new NoUsertExistsException();
        }

        student.setName(studentModifyRequest.getName());
        student.setEmail(studentModifyRequest.getEmail());
        student.setScore(studentModifyRequest.getScore());
        student.setRating(studentModifyRequest.getRating());

        studentRepository.updateStudent(student);

        return "redirect:/student/{id}";
    }

    @InitBinder("studentModifyRequest")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @ExceptionHandler(NoUsertExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void notFound() {
    }
}