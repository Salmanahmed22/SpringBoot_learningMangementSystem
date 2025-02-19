package com.example.demo.controller;

import com.example.demo.dtos.*;
import com.example.demo.models.*;
import com.example.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Admin Management
    @GetMapping
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        Admin admin = adminService.getAdminById(id);
        if (admin == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(admin);
    }

    @PostMapping
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin) {
        return ResponseEntity.ok(adminService.createAdmin(admin));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable Long id, @RequestBody Admin updatedAdmin) {
        try {
            Admin admin = adminService.updateAdmin(id, updatedAdmin);
            return ResponseEntity.ok(admin);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Delete User
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal User user,@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Instructor Management
    @PostMapping("/instructors")
    public ResponseEntity<Instructor> createInstructor(@AuthenticationPrincipal User user,@RequestBody Instructor instructor) {
        return ResponseEntity.ok(adminService.createInstructor(instructor));
    }

    @PutMapping("/instructors/{id}")
    public ResponseEntity<Instructor> updateInstructor(@AuthenticationPrincipal User user,@PathVariable Long id, @RequestBody Instructor updatedInstructor) {
        return ResponseEntity.ok(adminService.updateInstructor(id, updatedInstructor));
    }

    // Student Management
    @PostMapping("/students")
    public ResponseEntity<Student> createStudent(@AuthenticationPrincipal User user,@RequestBody Student student) {
        return ResponseEntity.ok(adminService.createStudent(student));
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<Student> updateStudent(@AuthenticationPrincipal User user,@PathVariable Long id, @RequestBody StudentDTO updatedStudentDTO) {
        return ResponseEntity.ok(adminService.updateStudent(id, updatedStudentDTO));
    }


    // Course Management
    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@AuthenticationPrincipal User user, @RequestBody CourseDTO courseDTO) {
        return ResponseEntity.ok(adminService.createCourse(courseDTO));
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<Course> updateCourse(@AuthenticationPrincipal User user,@PathVariable Long id, @RequestBody CourseDTO updatedCourseDTO) {
        try {
            Course updatedCourse = adminService.updateCourse(id, updatedCourseDTO);
            return ResponseEntity.ok(updatedCourse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Return 400 if the course doesn't exist or validation fails
        }
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@AuthenticationPrincipal User user,@PathVariable Long id) {
        adminService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    // Lesson Management
    @PostMapping("/lessons")
    public ResponseEntity<Lesson> createLesson(@AuthenticationPrincipal User user,@RequestBody LessonDTO lessonDTO) {
        return ResponseEntity.ok(adminService.createLesson(lessonDTO));
    }

    @PutMapping("/lessons/{id}")
    public ResponseEntity<Lesson> updateLesson(@AuthenticationPrincipal User user,@PathVariable Long id, @RequestBody LessonDTO updatedLessonDTO) {
        return ResponseEntity.ok(adminService.updateLesson(id, updatedLessonDTO));
    }

    @DeleteMapping("/lessons/{id}")
    public ResponseEntity<Void> deleteLesson(@AuthenticationPrincipal User user,@PathVariable Long id) {
        adminService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }

    // Assignment Management
    @PostMapping("/assignments")
    public ResponseEntity<Assignment> createAssignment(@AuthenticationPrincipal User user,@RequestBody AssignmentDTO assignmentDTO) {
        return ResponseEntity.ok(adminService.createAssignment(assignmentDTO));
    }

    @PutMapping("/assignments/{id}")
    public ResponseEntity<Assignment> updateAssignment(@AuthenticationPrincipal User user,@PathVariable Long id, @RequestBody AssignmentDTO updatedAssignmentDTO) {
        return ResponseEntity.ok(adminService.updateAssignment(id, updatedAssignmentDTO));
    }

    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<Void> deleteAssignment(@AuthenticationPrincipal User user,@PathVariable Long id) {
        adminService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }

    // Quiz Management
    @PostMapping("/quizzes")
    public ResponseEntity<Quiz> createQuiz(@RequestBody QuizDTO quizDTO) {
        return ResponseEntity.ok(adminService.createQuiz(quizDTO));
    }

    @PutMapping("/quizzes/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long id, @RequestBody QuizDTO updatedQuizDTO) {
        return ResponseEntity.ok(adminService.updateQuiz(id, updatedQuizDTO));
    }

    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        adminService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

    // Question Management
    @PostMapping("/questions/{questionBankId}")
    public ResponseEntity<Question> createQuestion(@PathVariable Long questionBankId, @RequestBody QuestionDTO questionDTO) {
        return ResponseEntity.ok(adminService.createQuestion(questionBankId, questionDTO));
    }

    @PutMapping("/questions/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @RequestBody QuestionDTO updatedQuestionDTO) {
        return ResponseEntity.ok(adminService.updateQuestion(id, updatedQuestionDTO));
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        adminService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    // Submission Management
    


}
