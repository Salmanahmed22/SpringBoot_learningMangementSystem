package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor

public class AssignmentDTO {

    @NotBlank(message = "Title is required and cannot be blank")
    private String title;

    @NotBlank(message = "Description is required and cannot be blank")
    private String description;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    private LocalDateTime dueDate;
    private Map<Long, String> submissions;
    private Long courseId;
    public AssignmentDTO() {
        submissions = new HashMap<>();
    }
}


