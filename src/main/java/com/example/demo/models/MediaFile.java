package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class MediaFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filePath; // Store the file path here

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonBackReference
    private Course course; // Each media file will be associated with a course

    public MediaFile() {
    }

    public MediaFile(String filePath, Course course) {
        this.filePath = filePath;
        this.course = course;
    }
}
