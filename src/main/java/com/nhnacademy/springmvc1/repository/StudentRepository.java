package com.nhnacademy.springmvc1.repository;

import com.nhnacademy.springmvc1.domain.Student;

public interface StudentRepository {
    boolean exists(String id);

    boolean matches(String id, String password);

    Student register(String id, String pwd, String name, String email, int score, String rating);

    Student getStudent(String id);

    Student updateStudent(Student student);
}
