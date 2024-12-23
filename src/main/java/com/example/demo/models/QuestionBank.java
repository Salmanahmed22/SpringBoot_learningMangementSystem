package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class QuestionBank {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @OneToMany(mappedBy = "questionBank", cascade = CascadeType.ALL)
    private List<Question> questions;

    @OneToOne
    @JsonBackReference
    private Course course;

    // Default constructor
    public QuestionBank() {
        this.questions = new ArrayList<>();
    }


}
