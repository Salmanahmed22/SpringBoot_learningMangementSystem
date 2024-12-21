package com.example.demo.models;

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
    private List<Course> courses;

    public Instructor() {
        super();
        courses = new ArrayList<>();
        department = "default department";
        employeeId = "default employeeId";
    }



    public void addCourse(Course course){
        courses.add(course);
        course.setInstructor(this);
    }

}
