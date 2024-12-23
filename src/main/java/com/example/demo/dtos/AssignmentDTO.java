package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor

public class AssignmentDTO {

    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Map<Long, String> submissions;
    private Long courseId;
    public AssignmentDTO() {
        submissions = new HashMap<>();
    }
}


