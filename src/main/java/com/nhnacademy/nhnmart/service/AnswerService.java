package com.nhnacademy.nhnmart.service;

import com.nhnacademy.nhnmart.domain.Answer;
import com.nhnacademy.nhnmart.exception.AnswerNotExistsException;
import com.nhnacademy.nhnmart.repository.answer.AnswerRepository;
import com.nhnacademy.nhnmart.repository.inquiry.InquiryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AnswerService {
    private final InquiryRepository inquiryRepository;
    private final AnswerRepository answerRepository;

    public AnswerService(InquiryRepository inquiryRepository, AnswerRepository answerRepository) {
        this.inquiryRepository = inquiryRepository;
        this.answerRepository = answerRepository;
    }

    public void addAnswerToInquiry(Answer answer, int inquiryId, String adminId) {
        answer.setAdminId(adminId);
        answerRepository.addByInquiryId(answer);
        inquiryRepository.addAnswer(inquiryId, answer.getId());
        log.info("Answer added successfully for inquiry with id: {}", inquiryId);
    }

    public Answer getAnswerById(int answerId) {
        if (answerRepository.findByAnswerId(answerId) != null) {
            return answerRepository.findByAnswerId(answerId);
        } else {
            throw new AnswerNotExistsException();
        }
    }

}
