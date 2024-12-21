package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class QuestionBank {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "questionBank", cascade = CascadeType.ALL)
    private List<Question> questions;

    // Default constructor
    public QuestionBank() {}

    public void addQuestion(Question question) {
        question.setQuestionBank(this);
        questions.add(question);
    }

    public void removeQuestion(Question question) {
        question.setQuestionBank(null);
        questions.remove(question);
    }
}
