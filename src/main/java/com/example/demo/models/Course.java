package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private short minLevel;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
//    @JsonManagedReference

    @JsonBackReference

    private Instructor instructor;

    @ManyToMany(mappedBy = "enrolledCourses")
    private List<Student> enrolledStudents;

    @OneToMany(mappedBy = "course")
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "course")
    private List<Assignment> assignments;

    @OneToMany(mappedBy = "course")
    private List<Quiz> quizzes;

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
