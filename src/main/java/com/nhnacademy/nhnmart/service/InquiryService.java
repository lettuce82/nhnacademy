package com.nhnacademy.nhnmart.service;

import com.nhnacademy.nhnmart.domain.Inquiry;
import com.nhnacademy.nhnmart.exception.CategoryNotExistsException;
import com.nhnacademy.nhnmart.exception.InquiryNotExistsException;
import com.nhnacademy.nhnmart.repository.inquiry.InquiryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class InquiryService {
    private final InquiryRepository inquiryRepository;

    public InquiryService(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }

    public Inquiry findInquiryById(int inquiryId) {
        Inquiry inquiry = inquiryRepository.findByInquiryId(inquiryId);
        if (inquiry == null) {
            log.error("Inquiry not found for id: {}", inquiryId);
            throw new InquiryNotExistsException();
        }
        return inquiry;
    }

    public List<Inquiry> findUnansweredInquiries() {
        return inquiryRepository.findUnansweredInquiries();
    }

    public List<Inquiry> getUnansweredInquiries() {
        return inquiryRepository.findUnansweredInquiries();
    }

    public List<Inquiry> getAllInquiriesByUserAndCategory(String userId, Inquiry.InquiryCategory category) {
        if (category == null) {
            throw new CategoryNotExistsException();
        } else {
            return inquiryRepository.findAllByUserIdAndCategory(userId, category);
        }
    }

    public boolean hasAnswer(int inquiryId) {
        findInquiryById(inquiryId);
        return inquiryRepository.hasAnswer(inquiryId);
    }

    public void addInquiry(Inquiry inquiry, String cutomerId) {
        inquiry.setCustomerId(cutomerId);
        inquiryRepository.addInquiry(inquiry);
    }
}
