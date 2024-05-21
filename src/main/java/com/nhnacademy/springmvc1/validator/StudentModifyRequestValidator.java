package com.nhnacademy.springmvc1.validator;

import com.nhnacademy.springmvc1.domain.StudentModifyRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class StudentModifyRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return StudentModifyRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        StudentModifyRequest request = (StudentModifyRequest) target;

        String name = request.getName().trim();
        if (name.isEmpty()) {
            errors.rejectValue("name", "name.empty", "이름은 공백이 아닌 문자열이어야 합니다.");
        }

        String email = request.getEmail();
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errors.rejectValue("email", "email.invalid", "이메일 형식이 아닙니다.");
        }

        int score = request.getScore();
        if (score < 0 || score > 100) {
            errors.rejectValue("score", "score.range", "점수는 0점 이상 100점 이하여야 합니다.");
        }

        String rating = request.getRating().trim();
        if (rating.isEmpty() || rating.length() > 200) {
            errors.rejectValue("rating", "rating.invalid", "평가는 공백이 아니고 200자 이하여야 합니다.");
        }
    }
}