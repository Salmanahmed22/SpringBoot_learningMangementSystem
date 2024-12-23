package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class QuizDTO {

    @NotBlank(message = "Title is required and cannot be blank")
    private String title;

    @NotBlank(message = "Description is required and cannot be blank")
    private String description;

    @NotNull(message = "Quiz date is required")
    @Future(message = "Quiz date must be in the future")
    private LocalDateTime quizDate;

    @NotNull(message = "Duration is required")
    private Duration duration;

    @NotNull(message = "Number of questions is required")
    @Positive(message = "Number of questions must be positive")
    private int numOfQuestions;

    private Long courseId;

    // Default constructor
    public QuizDTO() {}
}
