package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    private String department;
    private String employeeId;

    @OneToMany(mappedBy = "instructor")
    @JsonManagedReference
    private List<Course> courses;

    public Instructor() {
        super();
        courses = new ArrayList<>();
        department = "default department";
        employeeId = "default employeeId";
    }

}
