package com.example.demo.models;

import jakarta.persistence.*;

@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    // default constructor
    public Quiz() {
    }

    // parameterized constructor
    public Quiz(String title, String description, Course course) {
        this.title = title;
        this.description = description;
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

    public void setCourse(Course course) {
        this.course = course;
    }
}
