package com.nhnacademy.springmvc1.repository;

import com.nhnacademy.springmvc1.domain.Student;
import com.nhnacademy.springmvc1.exception.StudentAlreadyExistsException;
import com.nhnacademy.springmvc1.exception.StudentNotExistsException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

    private final Map<String, Student> studentMap = new HashMap<>();

    public StudentRepositoryImpl() {
        studentMap.put("hong", Student.create("hong", "1234", "홍길동", "gildong@nhnacademy.com", 100, "good"));
    }

    @Override
    public boolean exists(String id) {
        return studentMap.containsKey(id);
    }

    @Override
    public boolean matches(String id, String password) {
        return Optional.ofNullable(getStudent(id))
                .map(student -> student.getPwd().equals(password))
                .orElse(false);
    }

    @Override
    public Student register(String id, String pwd, String name, String email, int score, String rating) {
        if (exists(id)) {
            throw new StudentAlreadyExistsException("Student with id " + id + " already exists");
        }

        Student student = Student.create(id, pwd, name, email, score, rating);
        studentMap.put(id, student);

        return student;
    }


    @Override
    public List<Student> getStudents() {
        return studentMap.values().stream().toList();
    }

    @Override
    public Student getStudent(String id) {
        return studentMap.get(id);
    }

    @Override
    public Student updateStudent(String id, String pwd, String name, String email, int score, String rating) {
        if (!exists(id)) {
            throw new StudentNotExistsException("Student with id " + id + " does not exist");
        } else {
            Student student = getStudent(id);
            student.setPwd(pwd);
            student.setName(name);
            student.setEmail(email);
            student.setScore(score);
            student.setRating(rating);
            return student;
        }
    }
}
