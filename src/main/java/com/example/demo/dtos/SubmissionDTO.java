package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SubmissionDTO {
    private List<AnswerDTO> answers;

    // Default constructor
    public SubmissionDTO() {}
}