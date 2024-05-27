package com.nhnacademy.nhnmart.controller;

import com.nhnacademy.nhnmart.domain.User;
import com.nhnacademy.nhnmart.exception.NoUserExistsException;
import com.nhnacademy.nhnmart.exception.UserNotExistsException;
import com.nhnacademy.nhnmart.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class UserLoginController {
    private final UserService userService;

    public UserLoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/cs/login")
    public String login(@CookieValue(value = "SESSION", required = false) String sessionId,
                        Model model, HttpServletRequest request) {
        if (StringUtils.hasText(sessionId)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                String id = (String) session.getAttribute("userId");
                if (StringUtils.hasText(id)) {
                    model.addAttribute("id", id);
                    if (userService.getUser(id).getRole().equals(User.Role.CUSTOMER)) {
                        return "redirect:/cs";
                    } else if (userService.getUser(id).getRole().equals(User.Role.ADMIN)) {
                        return "redirect:/cs/admin";
                    }
                } else {
                    throw new NoUserExistsException();
                }
            }
        }
        return "loginForm";
    }

    @PostMapping("/cs/login")
    public String doLogin(@RequestParam("id") String id,
                          @RequestParam("pwd") String pwd,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          ModelMap modelMap) throws UserNotExistsException {

        if (userService.match(id, pwd)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", id);

            Cookie cookie = new Cookie("SESSION", session.getId());
            response.addCookie(cookie);

            modelMap.put("id", id);
            if (userService.getUser(id).getRole().equals(User.Role.CUSTOMER)) {
                return "redirect:/cs";
            }else {
                return "redirect:/cs/admin";
            }
        } else {
            throw new UserNotExistsException(id);
        }
    }

    @GetMapping("/cs/logout")
    public String logout(@CookieValue(value = "SESSION", required = false) String sessionId,
                         HttpServletRequest request, HttpServletResponse response) {

        if (StringUtils.hasText(sessionId)) {
            HttpSession session = request.getSession(false);

            if (session != null) {
                session.removeAttribute("userId");
                session.invalidate();
            }

            Cookie cookie = new Cookie("SESSION", null);
            cookie.setMaxAge(0);
            cookie.setPath("/cs/login");
            response.addCookie(cookie);
        }
        return "logoutView";
    }

}