package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
    @MapKeyColumn(name = "assignment_id")
    @Column(name = "assignment_grade")
    private Map<Long, String> assginmentsGrades;

    @ElementCollection
    @MapKeyColumn(name = "quiz_id")
    @Column(name = "quiz_grade")
    private Map<Long, String> quizGrades;

    @Column(nullable = false)
    private short level;

    // default constructor
    public Student() {
        super();
        assginmentsGrades = new HashMap<>();
        enrolledCourses = new ArrayList<>();
        level = 1;
    }


}
