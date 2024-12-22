package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor

public class AssignmentRequest {

    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Map<Long, String> submissions;
    private Long courseId; // Only include the course ID to avoid circular references
    public AssignmentRequest() {}
}


