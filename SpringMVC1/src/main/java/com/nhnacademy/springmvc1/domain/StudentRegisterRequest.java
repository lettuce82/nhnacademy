package com.nhnacademy.springmvc1.domain;

import lombok.Value;

@Value
public class StudentRegisterRequest {
    String id;
    String pwd;
    String name;
    String email;
    int score;
    String rating;
}
