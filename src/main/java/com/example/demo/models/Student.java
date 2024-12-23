package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Student extends User {
    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @JsonManagedReference(value = "student-course")
    private List<Course> enrolledCourses;


    @ElementCollection
    @MapKeyColumn(name = "Assignment_id")
    @Column(name = "final_grade")
    private Map<Long, String> grades;

    @Column(nullable = false)
    private short level;

    // default constructor
    public Student() {
        super();
        grades = new HashMap<>();
        enrolledCourses = new ArrayList<>();
        level = 1;
    }


}
