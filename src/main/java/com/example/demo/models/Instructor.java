package com.example.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Instructor extends User {

    // Getters and Setters
    @Getter
    @Setter
    private String department;
    @Getter
    @Setter
    private String employeeId;

    @OneToMany(mappedBy = "instructor")
    private List<Course> courses;

    public Instructor() {}

    public Instructor(String name, String email, String password, Role role, String department, String employeeId) {
        super(name, email, password, role);
        this.department = department;
        this.employeeId = employeeId;
        this.courses = new ArrayList<>();
    }

    public void addCourse(Course course){
        courses.add(course);
        course.setInstructor(this);
    }

}