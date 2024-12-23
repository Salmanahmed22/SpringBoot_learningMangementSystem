package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AnswerDTO {
    private long id;
    private Long questionId;
    private String answer;

    // Default constructor
    public AnswerDTO() {}
}
