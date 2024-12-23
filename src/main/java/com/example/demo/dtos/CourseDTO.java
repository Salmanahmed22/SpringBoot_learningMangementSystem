package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CourseDTO {
    private short minLevel;
    private String title;
    private String description;

    public CourseDTO() {

    }
    private Long instructorId = null;
}

