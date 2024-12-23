package com.example.demo.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class InstructorDTO {
    private String department;
    private String employeeId;
    private String name;
    private String email;
    private String password;

    public InstructorDTO() {

    }
}
