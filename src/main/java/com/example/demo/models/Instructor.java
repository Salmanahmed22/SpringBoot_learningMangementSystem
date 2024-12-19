package com.example.demo.models;

import jakarta.persistence.Entity;

@Entity
public class Instructor extends User {

    private String department;
    private String employeeId;

    public Instructor() {}

    public Instructor(String name, String email, String password, Role role, String department, String employeeId) {
        super(name, email, password, role);
        this.department = department;
        this.employeeId = employeeId;
    }

    // Getters and Setters
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
