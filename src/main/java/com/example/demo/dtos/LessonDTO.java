package com.example.demo.dtos;

import com.example.demo.models.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LessonDTO {

    private String title;
    private String content;
    private Long courseId;

    // Default constructor for serialization/deserialization
    public LessonDTO() {
    }
}
