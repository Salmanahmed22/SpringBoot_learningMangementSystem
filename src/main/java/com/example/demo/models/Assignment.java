package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor

@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private LocalDateTime dueDate;

    @ElementCollection
    @MapKeyColumn(name = "student_id")
    @Column(name = "submission_content")
    private Map<Long, String> submissions = new HashMap<>();

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonBackReference(value = "course-assignment")
    private Course course;

    // default constructor
    public Assignment() {
    }

}
