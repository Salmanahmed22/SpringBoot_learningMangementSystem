package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MediaFile> mediaFiles = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "instructor_id")
    @JsonBackReference(value = "instructor_course")
    private Instructor instructor;

    @ManyToMany(mappedBy = "enrolledCourses")
    @JsonBackReference(value = "student-course")
    private List<Student> enrolledStudents;

    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST)
    @JsonManagedReference(value = "course-lesson")
    private List<Lesson> lessons;


    @OneToMany(mappedBy = "course")
    @JsonManagedReference(value = "course-assignment")
    private List<Assignment> assignments;

    @OneToMany(mappedBy = "course")
    @JsonManagedReference(value = "course-quiz")
    private List<Quiz> quizzes;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference(value = "course-questionBank")
    private QuestionBank questionBank;


    public Course() {
        this.questionBank = new QuestionBank();
        this.enrolledStudents = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.assignments = new ArrayList<>();
        this.quizzes = new ArrayList<>();
    }


}
