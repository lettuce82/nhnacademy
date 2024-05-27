package com.nhnacademy.nhnmart.repository.inquiry;

import com.nhnacademy.nhnmart.domain.Inquiry;

import java.util.List;

public interface InquiryRepository {
    boolean hasAnswer(int inquiryId);

    Inquiry findByInquiryId(int inquiryId);
    Inquiry addInquiry(Inquiry inquiry);
    Inquiry addAnswer(int inquiryId, int answerId);
    List<Inquiry> findUnansweredInquiries();
    List<Inquiry> findAllByUserId(String userId);
    List<Inquiry> findAllByUserIdAndCategory(String userId, Inquiry.InquiryCategory category);
}