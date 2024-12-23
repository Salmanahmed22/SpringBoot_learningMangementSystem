package com.example.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class QuestionDTO {

    @NotBlank(message = "Question must not be blank")
    private String question;

    @NotEmpty(message = "Options must not be empty")
    private List<String> options;

    @NotBlank(message = "Correct answer must not be blank")
    private String correctAnswer;

    QuestionDTO() {}
}
