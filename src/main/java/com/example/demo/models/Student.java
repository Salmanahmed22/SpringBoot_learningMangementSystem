package com.example.demo.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Student extends User {

    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> enrolledCourses;

    // default constructor
    public Student() {
    }

    // parameterized constructor
    public Student(String name, String email, String password, Role role, List<Course> enrolledCourses) {
        super(name, email, password, role);
        this.enrolledCourses = enrolledCourses;
    }

    // Getters
    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    // Setters
    public void setEnrolledCourses(List<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    // enroll in a course
    public void enrollCourse(Course course) {
        this.enrolledCourses.add(course);
    }

    // unenroll from a course
    public void unenrollCourse(Course course) {
        this.enrolledCourses.remove(course);
    }
}
