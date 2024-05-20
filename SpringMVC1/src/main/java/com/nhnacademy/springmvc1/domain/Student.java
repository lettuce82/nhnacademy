package com.nhnacademy.springmvc1.domain;

import lombok.Data;

@Data
public class Student {
    private String id;
    private String pwd;
    private String name;
    private String email;
    private int score;
    private String rating;

    public static Student create(String id, String password, String name, String email, int score, String rating) {
        return new Student(id, password, name, email, score, rating);
    }

    private Student(String id, String pwd, String name, String email, int score, String rating) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.email = email;
        this.score = score;
        this.rating = rating;
    }
    private static final String MASK = "*****";

    public static Student constructPasswordMaskedUser(Student student) {
        Student newStudent = Student.create(student.getId(), MASK, student.getName(), student.getEmail(), student.getScore(), student.getRating());

        return newStudent;
    }

}
