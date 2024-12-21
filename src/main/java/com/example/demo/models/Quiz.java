package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime deadline;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "quiz_id")
    private List<Question> questions;

//    @ElementCollection
//    private HashMap<String, List<Answer>> submissions = new HashMap<String, List<Answer>>();

    // Default constructor
    public Quiz() {}

//    public void submitAnswers(String studentId, List<Answer> answers) {
//        if (submissions.containsKey(studentId)) {
//            throw new IllegalArgumentException("You have already submitted this quiz.");
//        }
//        if (deadline != null && deadline.isBefore(LocalDateTime.now())) {
//            throw new IllegalArgumentException("The deadline for this quiz has passed.");
//        }
//        submissions.put(studentId, answers);
//    }
//
//    // Method to retrieve a student's submission
//    public List<Answer> getSubmission(Long studentId) {
//        return submissions.get(studentId);
//    }
}
