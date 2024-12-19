package com.example.demo.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private short minLevel;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Duration duration;

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
    }

    // parameterized constructor
    public Course(String title, String description, Duration duration, List<Student> enrolledStudents) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.enrolledStudents = enrolledStudents;
    }

    // Getters
    public String getId() {
        return id;
    }

    public short getMinLevel() {return minLevel;}

    public String getTitle() {return title;}

    public String getDescription() {return description;}

    public Duration getDuration() {return duration;}

    public List<Student> getEnrolledStudents() {return enrolledStudents;}

    public List<Lesson> getLessons() {return lessons;}

    public List<Assignment> getAssignments() {return assignments;}

    public List<Quiz> getQuizzes() {return quizzes;}

    public Instructor getInstructor() {return instructor;}
    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setMinLevel(short minLevel) { this.minLevel = minLevel;}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setEnrolledStudents(List<Student> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public void setInstructor(Instructor instructor) {this.instructor = instructor;}

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
        lesson.setCourse(this);
    }
}
