package com.example.demo.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Date dueDate;

    @ElementCollection
    @MapKeyColumn(name = "student_id")
    @Column(name = "submission_content")
    private Map<Long, String> submissions = new HashMap<>();

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    // default constructor
    public Assignment() {
    }

    // parameterized constructor
    public Assignment(String title, String description, Date dueDate, Course course) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.course = course;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Course getCourse() {
        return course;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void submitAssignment(Long studentId, String submissionContent) {
        submissions.put(studentId, submissionContent);
    }
}
