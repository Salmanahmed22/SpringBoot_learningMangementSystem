package com.example.demo.user.student;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    public List<Student> getStudents() {
        return List.of(
                new Student(1L,
                        "Omar",
                        "omar@gmail.com"
                        ,"123")
        );
    }
}
