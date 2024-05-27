package com.nhnacademy.nhnmart.controller;

import com.nhnacademy.nhnmart.domain.Answer;
import com.nhnacademy.nhnmart.domain.File;
import com.nhnacademy.nhnmart.domain.Inquiry;
import com.nhnacademy.nhnmart.domain.User;
import com.nhnacademy.nhnmart.exception.ValidationFailedException;
import com.nhnacademy.nhnmart.service.AnswerService;
import com.nhnacademy.nhnmart.service.FileService;
import com.nhnacademy.nhnmart.service.InquiryService;
import com.nhnacademy.nhnmart.service.UserService;
import com.nhnacademy.nhnmart.validator.AnswerRequestValidator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/cs/admin")
public class AdminController {
    private final UserService userService;
    private final InquiryService inquiryService;
    private final AnswerService answerService;
    private final FileService fileService;
    private final AnswerRequestValidator answerValidator;

    public AdminController(UserService userService, InquiryService inquiryService,
                           AnswerService answerService, FileService fileService,
                           AnswerRequestValidator answerValidator) {
        this.userService = userService;
        this.inquiryService = inquiryService;
        this.answerService = answerService;
        this.fileService = fileService;
        this.answerValidator = answerValidator;
    }

    @InitBinder("answer")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(answerValidator);
    }

    @ModelAttribute("user")
    public User user(@SessionAttribute(value = "userId", required = false) String sessionId) {
        return userService.getUser(sessionId);
    }

    @GetMapping
    public String getUnansweredInquiries(Model model) {
        List<Inquiry> unansweredInquiries = inquiryService.findUnansweredInquiries();
        model.addAttribute("unansweredInquiries", unansweredInquiries);
        return "admin/inquires";
    }

    @GetMapping("/answer/{inquiryId}")
    public String getAnswerForm(@PathVariable int inquiryId, Model model) {
        inquiryService.findInquiryById(inquiryId);

        model.addAttribute("inquiry", inquiryService.findInquiryById(inquiryId));
        model.addAttribute("hasAnswer", inquiryService.hasAnswer(inquiryId));

        if (fileService.hasFile(inquiryId)) {
            List<File> attachedFiles =  fileService.findByInquiry(inquiryId);
            model.addAttribute("attachedFiles", attachedFiles);
        }

        return "admin/answerForm";
    }

    @PostMapping("/answer")
    public String addAnswer(@Valid @ModelAttribute("answer") Answer answer,
                            BindingResult bindingResult,
                            @RequestParam("inquiryId") int inquiryId,
                            @ModelAttribute("user") User user) {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        answerService.addAnswerToInquiry(answer, inquiryId, user.getId());

        return "redirect:/cs/admin";
    }
}