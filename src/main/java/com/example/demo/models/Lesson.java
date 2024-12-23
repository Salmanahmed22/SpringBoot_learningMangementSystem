package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@Entity
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String content;


    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonBackReference(value = "course-lesson")
    private Course course;

    @OneToMany
    private List<Student> Attendance;

    public Lesson() {
        this.Attendance = new ArrayList<>();
    }
}
