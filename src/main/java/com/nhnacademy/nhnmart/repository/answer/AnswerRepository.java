package com.nhnacademy.nhnmart.repository.answer;

import com.nhnacademy.nhnmart.domain.Answer;

public interface AnswerRepository {
    Answer findByAnswerId(int answerId);
    Answer addByInquiryId(Answer answer);
}
