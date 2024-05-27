package com.nhnacademy.nhnmart.validator;

import com.nhnacademy.nhnmart.domain.Inquiry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class InquiryRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Inquiry.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Inquiry request = (Inquiry) target;

        String title = request.getTitle();
        if (title != null) {
            title = title.trim();

            int titleLength = title.length();
            if (titleLength < 2 || titleLength > 200) {
                errors.rejectValue("title", "inquiry.title.invalid",
                        new Object[]{2, 200, titleLength},
                        "제목의 길이가 유효하지 않습니다. (2~200자 허용)");
            }
        }

        String content = request.getContent();
        if (content != null) {
            content = content.trim();
            int contentLength = content.length();
            if (contentLength < 1 || contentLength > 40000) {
                errors.rejectValue("content", "inquiry.content.invalid",
                        new Object[]{1, 40000, contentLength},
                        "본문의 길이가 유효하지 않습니다. (1~40000자 허용)");
            }
        }
    }
}