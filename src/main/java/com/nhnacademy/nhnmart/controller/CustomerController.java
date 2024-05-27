package com.nhnacademy.nhnmart.controller;

import com.nhnacademy.nhnmart.converter.FileConverter;
import com.nhnacademy.nhnmart.domain.Answer;
import com.nhnacademy.nhnmart.domain.File;
import com.nhnacademy.nhnmart.domain.Inquiry;
import com.nhnacademy.nhnmart.domain.User;
import com.nhnacademy.nhnmart.exception.ValidationFailedException;
import com.nhnacademy.nhnmart.service.AnswerService;
import com.nhnacademy.nhnmart.service.FileService;
import com.nhnacademy.nhnmart.service.InquiryService;
import com.nhnacademy.nhnmart.service.UserService;
import com.nhnacademy.nhnmart.validator.InquiryRequestValidator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/cs")
public class CustomerController {
    private final UserService userService;
    private final InquiryService inquiryService;
    private final AnswerService answerService;
    private final FileService fileService;
    private final InquiryRequestValidator validator;

    public CustomerController(UserService userService, InquiryService inquiryService,
                              AnswerService answerService, FileService fileService,
                              FileConverter fileConverter, InquiryRequestValidator validator) {
        this.userService = userService;
        this.inquiryService = inquiryService;
        this.answerService = answerService;
        this.fileService = fileService;
        this.validator = validator;
    }

    @ModelAttribute("user")
    public User user(@SessionAttribute(value = "userId", required = false) String sessionId) {
        return userService.getUser(sessionId);
    }

    @GetMapping
    public String getInquiries(@ModelAttribute("user") User user,
                               @RequestParam(value = "category", required = false) Inquiry.InquiryCategory category,
                               Model model) {
        List<Inquiry> inquiries = new ArrayList<>();

        if (category == null) {
            inquiries = inquiryService.getUnansweredInquiries();
        } else {
            inquiries = inquiryService.getAllInquiriesByUserAndCategory(user.getId(), category);
        }

        List<Boolean> hasAnswerList = new ArrayList<>();
        for (Inquiry inquiry : inquiries) {
            boolean hasAnswer = inquiryService.hasAnswer(inquiry.getId());
            hasAnswerList.add(hasAnswer);
        }

        model.addAttribute("inquiries", inquiries);
        model.addAttribute("hasAnswerList", hasAnswerList);
        model.addAttribute("selectedCategory", category);
        return "customer/inquiries";
    }


    @GetMapping("/inquiry")
    public String getInquiryForm(Model model) {
        model.addAttribute("inquiry", new Inquiry());
        return "customer/inquiryForm";
    }

    @PostMapping("/inquiry")
    public String postInquiry(@Valid @ModelAttribute("inquiry") Inquiry inquiry,
                              BindingResult bindingResult,
                              @ModelAttribute("user") User user,
                              @RequestParam("attachedFiles") List<MultipartFile> attachedFiles) throws IOException {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        inquiryService.addInquiry(inquiry, user.getId());

        if (attachedFiles != null && !attachedFiles.isEmpty()) {
            fileService.save(attachedFiles, inquiry.getId());
        }

        return "redirect:/cs";
    }

    @GetMapping("/inquiry/{id}")
    public String getInquiryDetail(@PathVariable int id, Model model) {
        Inquiry inquiry = inquiryService.findInquiryById(id);
        model.addAttribute("inquiry", inquiry);

        boolean hasAnswer = inquiryService.hasAnswer(id);
        model.addAttribute("hasAnswer", hasAnswer);

        if (hasAnswer) {
            Answer answer = answerService.getAnswerById(inquiry.getAnswerId());
            model.addAttribute("answer", answer);
            String adminId = String.valueOf(answer.getAdminId());
            model.addAttribute("admin", userService.getUser(adminId));
        }

        List<File> attachedFiles =  fileService.findByInquiry(id);
        model.addAttribute("attachedFiles", attachedFiles);
        return "customer/inquiryDetail";
    }


    @InitBinder("inquiry")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }
}
