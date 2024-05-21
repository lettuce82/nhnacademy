package com.nhnacademy.springmvc1.domain;

import lombok.Value;

@Value
public class StudentModifyRequest {
    String name;
    String email;
    int score;
    String rating;
}