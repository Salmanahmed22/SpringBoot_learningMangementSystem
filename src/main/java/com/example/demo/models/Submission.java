package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    Student student;

    @OneToMany
    @JoinColumn(name = "answer_id")
    List<Answer> answers;

    // Default constructor
    public Submission() {}
}
