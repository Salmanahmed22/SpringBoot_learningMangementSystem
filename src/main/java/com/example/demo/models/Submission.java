package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonBackReference
    Student student;

    @ElementCollection
    @CollectionTable(name = "submission_answers", joinColumns = @JoinColumn(name = "submission_id"))
    @Column(name = "answer")
    private List<String> answers;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    @JsonBackReference(value = "quiz-submission")
    Quiz quiz;

    private String grade = "";

    // Default constructor
    public Submission() {
        answers = new ArrayList<>();
    }
}
