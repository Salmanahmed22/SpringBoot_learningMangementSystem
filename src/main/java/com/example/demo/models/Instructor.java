package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Instructor extends User {

    private String department = "default department";
    private String employeeId = "default employeeId";

    @OneToMany(mappedBy = "instructor")
    @JsonManagedReference(value = "instructor-course")
    private List<Course> courses = new ArrayList<>();

    public Instructor() {
        super();
    }
}
