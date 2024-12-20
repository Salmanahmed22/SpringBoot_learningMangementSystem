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
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String text;

    @ElementCollection
    private List<String> options; // Multiple-choice options

    private String correctAnswer; // The correct answer

    @ManyToOne
    @JoinColumn(name = "question_bank_id") // Foreign key to QuestionBank
    private QuestionBank questionBank;

    // Default constructor
    public Question() {}

    // Method to validate an answer
    public boolean isCorrect(String answer) {
        return this.correctAnswer.equals(answer);
    }
}
