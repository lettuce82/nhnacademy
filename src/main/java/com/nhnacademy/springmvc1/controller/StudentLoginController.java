package com.nhnacademy.springmvc1.controller;

import com.nhnacademy.springmvc1.exception.StudentNotExistsException;
import com.nhnacademy.springmvc1.repository.StudentRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StudentLoginController {
    private final StudentRepository studentRepository;

    public StudentLoginController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/")
    public String login(@CookieValue(value = "SESSION", required = false) String sessionId,
                        Model model, HttpServletRequest request) {
        if (StringUtils.hasText(sessionId)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                String id = (String) session.getAttribute("studentId");
                if (StringUtils.hasText(id)) {
                    model.addAttribute("id", id);
                    return "redirect:/student/" + id;
                }
            }
        }
        return "loginForm";
    }

    @PostMapping("/")
    public String doLogin(@RequestParam("id") String id,
                          @RequestParam("pwd") String pwd,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          ModelMap modelMap) {

        if (studentRepository.matches(id, pwd)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("studentId", id);

            Cookie cookie = new Cookie("SESSION", id);
            response.addCookie(cookie);

            modelMap.put("id", id);
            return "redirect:/student/" + id;
        } else {
            throw new StudentNotExistsException(id);
        }
    }
}