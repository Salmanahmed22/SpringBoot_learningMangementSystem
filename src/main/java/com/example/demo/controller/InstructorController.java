package com.example.demo.controller;

import com.example.demo.dtos.*;
import com.example.demo.repository.MediaFileRepository;
import com.example.demo.models.*;
import com.example.demo.models.Notification;
import com.example.demo.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
@Validated
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private MediaFileRepository mediaFileRepository; // Autowire the MediaFileRepository

    // Get all instructors
    @GetMapping
    public ResponseEntity<List<Instructor>> getAllInstructors() {
        return ResponseEntity.ok(instructorService.getAllInstructors());
    }

    // Get an instructor by ID
    @GetMapping("/{id}")
    public ResponseEntity<Instructor> getInstructorById(@PathVariable Long id) {
        Instructor instructor = instructorService.getInstructorById(id);
        if (instructor != null) {
            return ResponseEntity.ok(instructor);
        }
        return ResponseEntity.notFound().build();
    }

    // Create a new instructor
    @PostMapping
    public ResponseEntity<Instructor> createInstructor(@RequestBody Instructor instructor) {
        return ResponseEntity.ok(instructorService.createInstructor(instructor));
    }

    // Update an existing instructor
    @PutMapping("/{id}")
    public ResponseEntity<Instructor> updateInstructor(@PathVariable Long id, @RequestBody Instructor updatedInstructor) {
        Instructor instructor = instructorService.updateInstructor(id, updatedInstructor);
        if (instructor != null) {
            return ResponseEntity.ok(instructor);
        }
        return ResponseEntity.notFound().build();
    }


    // Fetch notifications for the instructor
    @GetMapping("/{instructorId}/notifications")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long instructorId, @RequestParam(required = false) Boolean unread) {
        return ResponseEntity.ok(instructorService.getNotifications(instructorId, unread));
    }



    // Create a new course
    // Tested
    @PostMapping("/{instructorId}/courses")
    public ResponseEntity<Course> createCourse(@PathVariable Long instructorId, @RequestBody CourseDTO courseDTO) {
        return ResponseEntity.ok(instructorService.createCourse(instructorId, courseDTO));
    }

    // Tested
    @PutMapping("/{instructorId}/courses/{courseId}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long instructorId,
                                               @PathVariable Long courseId,
                                               @RequestBody CourseDTO updatedCourse) {
        Course course = instructorService.updateCourse(instructorId, courseId, updatedCourse);
        if (course != null) {
            return ResponseEntity.ok(course);
        }
        return ResponseEntity.notFound().build();
    }

    // Tested
    @DeleteMapping("/{instructorId}/courses/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long instructorId,
                                             @PathVariable Long courseId) {
        instructorService.deleteCourse(instructorId, courseId);
        return ResponseEntity.noContent().build();
    }

    // Tested
    @PostMapping("/{instructorId}/courses/{courseId}/lessons")
    public ResponseEntity<Course> addLessonToCourse(
            @PathVariable Long instructorId,
            @PathVariable Long courseId,
            @RequestBody LessonDTO lesson) {
        return ResponseEntity.ok(instructorService.addLessonToCourse(instructorId, courseId, lesson));
    }

    // Tested
    @PostMapping("/{instructorId}/courses/{courseId}/questionBank")
    public ResponseEntity<QuestionBank> addQuestionToBank(
            @PathVariable Long instructorId,
            @PathVariable Long courseId,
            @Valid @RequestBody QuestionDTO questionDTO) {
        return ResponseEntity.ok(instructorService.addQuestionToBank(instructorId, courseId, questionDTO));
    }


    // Tested
    @PostMapping("/{instructorId}/courses/{courseId}/quiz")
    public ResponseEntity<Quiz> addQuizToCourse(
            @PathVariable Long instructorId,
            @PathVariable Long courseId,
            @Valid @RequestBody QuizDTO quizDTO) {
        return ResponseEntity.ok(instructorService.createQuiz(instructorId, courseId, quizDTO));
    }

    // Tested
    @DeleteMapping("/{instructorId}/courses/{courseId}/students/{studentId}")
    public ResponseEntity<Void> removeStudentFromCourse(@PathVariable Long instructorId,
                                                        @PathVariable Long courseId,
                                                        @PathVariable Long studentId){
        instructorService.removeStudentFromCourse(instructorId, courseId, studentId);
        return ResponseEntity.noContent().build();
    }

    // Tested
    @PutMapping("/{instructorId}/edit")
    public ResponseEntity<Instructor> editInstructorProfile(@PathVariable Long instructorId, @RequestBody InstructorDTO instructorDTO) {
        return ResponseEntity.ok(instructorService.updateInstructorProfile(instructorId, instructorDTO));
    }


    // Endpoint to upload media file to a course
    @PostMapping("/{instructorId}/courses/{courseId}/media/upload")
    public ResponseEntity<String> uploadMediaToCourse(
            @PathVariable Long instructorId,
            @PathVariable Long courseId,
            @RequestParam("filePath") String filePath) {

        try {
            instructorService.saveMediaFile(instructorId, courseId, filePath);

            // Return a successful response
            return ResponseEntity.ok("File path saved successfully: " + filePath);
        }
        catch (RuntimeException e) {

            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to save file path: " + e.getMessage());
        }
    }

    // Tested
    @PostMapping("/{instructorId}/courses/{courseId}/assignments")
    public ResponseEntity<Course> addAssignmentToCourse(@PathVariable Long instructorId,
                                                   @PathVariable Long courseId,
                                                   @Valid @RequestBody AssignmentDTO assignmentDTO) {
        return ResponseEntity.ok(instructorService.addAssignmentToCourse(instructorId, courseId, assignmentDTO));
    }
    
}
