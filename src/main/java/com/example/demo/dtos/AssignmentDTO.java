package com.example.demo.dtos;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AssignmentDTO {

    @NotBlank(message = "Title is required and cannot be blank")
    private String title;

    @NotNull(message = "Mark is required")
    @Positive(message = "Mark must be positive")
    private int mark;

    @NotBlank(message = "Description is required and cannot be blank")
    private String description;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    private LocalDateTime dueDate;

    // Default constructor
    public AssignmentDTO() {}
}
