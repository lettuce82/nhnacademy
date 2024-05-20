package com.nhnacademy.springmvc1.repository;

import com.nhnacademy.springmvc1.domain.Student;

import java.util.List;

public interface StudentRepository {
    boolean exists(String id);

    boolean matches(String id, String password);

    Student register(String id, String pwd, String name, String email, int score, String rating);

    List<Student> getStudents();

    Student getStudent(String id);

    Student updateStudent(String id, String pwd, String name, String email, int score, String rating);
}
