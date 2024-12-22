package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LessonDTO {

    private Long id;
    private String title;
    private String content;
    private Long courseId;
    private List<Long> attendanceStudentIds;

    // Default constructor for serialization/deserialization
    public LessonDTO() {
    }
}
