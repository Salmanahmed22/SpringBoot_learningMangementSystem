package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class QuizDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime quizDate;
    private Duration duration;
    private Long courseId;
    private List<QuestionDTO> questionDTOs;

    // Default constructor
    public QuizDTO() {}

}
