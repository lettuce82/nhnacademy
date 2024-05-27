package com.nhnacademy.nhnmart.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    private int id; //autoincrement
    private String content;
    private LocalDateTime createdAt;
    private String adminId;
}