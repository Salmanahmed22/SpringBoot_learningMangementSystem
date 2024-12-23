package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class QuestionDTO {
    private String question;
    private List<String> options;
    private String correctAnswer;

    QuestionDTO() {}
}
