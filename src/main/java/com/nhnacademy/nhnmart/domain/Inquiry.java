package com.nhnacademy.nhnmart.domain;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Inquiry {
    @Setter
    private int id; //autoincrement
    private String title;
    private String content;
    private InquiryCategory category;
    private LocalDateTime createdAt;
    @Setter
    private String customerId;
    @Setter
    private Integer answerId;
    public enum InquiryCategory {
        COMPLAINT, SUGGESTION, REFUND_EXCHANGE, COMPLIMENT, OTHER
    }
}
