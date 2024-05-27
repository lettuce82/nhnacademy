package com.nhnacademy.nhnmart.validator;

import com.nhnacademy.nhnmart.domain.Answer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class AnswerRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Answer.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Answer request = (Answer) target;

        String title = request.getContent().trim();
        String content = request.getContent().trim();

        int contentLength = content.length();
        if (contentLength < 1 || contentLength > 40000) {
            errors.rejectValue("content", "inquiry.content.invalid",
                    new Object[]{1, 40000, contentLength},
                    "답변의 길이가 유효하지 않습니다. (1~40000자 허용)");
        }
    }
}