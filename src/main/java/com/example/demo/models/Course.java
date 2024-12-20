package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private short minLevel;

    private String title;

    private String description;


    @ManyToMany(mappedBy = "enrolledCourses")
    private List<Student> enrolledStudents;

    @OneToMany(mappedBy = "course")
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "course")
    private List<Assignment> assignments;

    @OneToMany(mappedBy = "course")
    private List<Quiz> quizzes;


    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    public Course() {
        this.enrolledStudents = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.assignments = new ArrayList<>();
        this.quizzes = new ArrayList<>();
    }



    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
        lesson.setCourse(this);
    }
}
