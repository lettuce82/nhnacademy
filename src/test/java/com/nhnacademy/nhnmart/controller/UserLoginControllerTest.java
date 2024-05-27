package com.nhnacademy.nhnmart.controller;

import com.nhnacademy.nhnmart.domain.User;
import com.nhnacademy.nhnmart.exception.NoUserExistsException;
import com.nhnacademy.nhnmart.service.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserLoginController.class)
public class UserLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    private User customer;
    private User admin;
    MockHttpSession session;

    @BeforeEach
    void setUp() {
        customer = new User("hong", "1234", "customer", User.Role.CUSTOMER);
        admin = new User("chae", "1234", "admin", User.Role.ADMIN);
        session = new MockHttpSession();
    }
    @Test
    void login_CustomerRole() throws Exception {
        when(userService.getUser("hong")).thenReturn(customer);
        when(userService.match("hong", "1234")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/cs/login")
                        .param("id", "hong")
                        .param("pwd", "1234")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/cs"));
    }

    @Test
    void getLoginForm() throws Exception {
        mockMvc.perform(get("/cs/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("loginForm"));
    }

    @Test
    void getLoginFormWithSessionCookie() throws Exception {
        mockMvc.perform(get("/cs/login")
                        .cookie(new Cookie("SESSION", "1234")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("loginForm"));
    }

    @Test
    void postLoginSuccess() throws Exception {
        when(userService.getUser("hong")).thenReturn(customer);
        when(userService.match("hong", "1234")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/cs/login")
                        .param("id", "hong")
                        .param("pwd", "1234")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/cs"));
    }

    @Test
    void testDoLogin_ValidCredentials() throws Exception {
        when(userService.getUser("hong")).thenReturn(customer);
        when(userService.match("hong", "1234")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/cs/login")
                        .param("id", "hong")
                        .param("pwd", "1234")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/cs"));
    }

    @Test
    void testLogin_InvalidSession() throws Exception {
        when(userService.getUser(anyString())).thenThrow(new NoUserExistsException());

        mockMvc.perform(MockMvcRequestBuilders.get("/cs/login")
                        .cookie(new Cookie("SESSION", "123456")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("loginForm"));
    }

    @Test
    void getLogoutForm_LoggedInUser() throws Exception {
        session.setAttribute("userId", customer.getId());
        mockMvc.perform(get("/cs/logout")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("logoutView"));
    }

    @Test
    void logout_WithValidSession() throws Exception {
        session.setAttribute("userId", customer.getId());
        mockMvc.perform(get("/cs/logout")
                        .session(session)
                        .cookie(new Cookie("SESSION", "123456")))
                .andExpect(status().isOk())
                .andExpect(view().name("logoutView"));
    }

    @Test
    void logout_WithInvalidSession() throws Exception {
        mockMvc.perform(get("/cs/logout"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }
}