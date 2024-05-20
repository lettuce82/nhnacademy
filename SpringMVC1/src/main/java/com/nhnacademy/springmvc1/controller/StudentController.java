package com.nhnacademy.springmvc1.controller;

import com.nhnacademy.springmvc1.domain.Student;
import com.nhnacademy.springmvc1.domain.StudentRegisterRequest;
import com.nhnacademy.springmvc1.exception.NoUsertExistsException;
import com.nhnacademy.springmvc1.repository.StudentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
@RequestMapping("/student")
public class StudentController {
    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping(value = "/{id}")
    public String viewStudent(@PathVariable("id") String id, Model model) {
        model.addAttribute("student", Student.constructPasswordMaskedUser(studentRepository.getStudent(id)));
        return "studentView";
    }

    @GetMapping(value = "/{id}", params = "hideScore")
    public String viewStudentWithoutScore(@PathVariable("id") String id, Model model) {
        model.addAttribute("isHide", true);
        model.addAttribute("student", Student.constructPasswordMaskedUser(studentRepository.getStudent(id)));
        return "studentView";
    }

    @ModelAttribute("student")
    public Student student(@CookieValue(value = "SESSION", required = false) String sessionId,
                           Model model, HttpServletRequest request) {
        if (StringUtils.hasText(sessionId)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                String id = (String) session.getAttribute("studentId");
                if (StringUtils.hasText(id)) {
                    model.addAttribute("id", id);
                    return Student.constructPasswordMaskedUser(studentRepository.getStudent(id));
                }
            }
        } else {
            throw new NoUsertExistsException("No session found");
        }
        return null;
    }

    @GetMapping("/{id}/modify")
    public String studentModifyForm(Model model, @ModelAttribute("student") Student student) {
        model.addAttribute("student", student);
        return "studentModify";
    }

    @PostMapping("/{id}/modify")
    public ModelAndView modifyStudent(@ModelAttribute StudentRegisterRequest studentRegisterRequest, @PathVariable("id") String id) {
        Student student = studentRepository.updateStudent(
                studentRegisterRequest.getId(),
                studentRegisterRequest.getPwd(),
                studentRegisterRequest.getName(),
                studentRegisterRequest.getEmail(),
                studentRegisterRequest.getScore(),
                studentRegisterRequest.getRating()
        );

        ModelAndView mav = new ModelAndView("studentView");
        mav.addObject("student", Student.constructPasswordMaskedUser(student));

        return mav;
    }

}
