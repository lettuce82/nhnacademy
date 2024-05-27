package com.nhnacademy.nhnmart.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class User {
    private String id;
    private String password;
    private String name;
    private Role role;
    public enum Role {
        CUSTOMER, ADMIN
    }
}