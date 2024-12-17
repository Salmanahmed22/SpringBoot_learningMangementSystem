package com.example.demo.controller;

import com.example.demo.models.Course;
import com.example.demo.models.Student;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {

        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.createStudent(student));
    }

    @PutMapping("/{id}/enroll")
    public ResponseEntity<Student> enrollCourse(@PathVariable Long id, @RequestBody Course course) {
        return ResponseEntity.ok(studentService.enrollCourse(id, course));
    }

    @PutMapping("/{id}/unenroll")
    public ResponseEntity<Student> unenrollCourse(@PathVariable Long id, @RequestBody Course course) {
        return ResponseEntity.ok(studentService.unenrollCourse(id, course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
