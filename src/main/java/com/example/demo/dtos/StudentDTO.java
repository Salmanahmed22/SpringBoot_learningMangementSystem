package com.example.demo.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class StudentDTO {
    private String name;
    private String email;
    private short level;
    private String password;
}
