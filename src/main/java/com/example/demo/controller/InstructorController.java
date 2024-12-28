package com.example.demo.controller;

import com.example.demo.dtos.*;
import com.example.demo.repository.MediaFileRepository;
import com.example.demo.models.*;
import com.example.demo.models.Notification;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.MediaFileRepository;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.dtos.CourseDTO;
import com.example.demo.dtos.LessonDTO;
import com.example.demo.dtos.QuestionDTO;
import com.example.demo.dtos.QuizDTO;
import com.example.demo.models.*;
//import com.example.demo.models.Notification;
import com.example.demo.models.Quiz;
import com.example.demo.service.InstructorService;
import com.example.demo.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.MediaFile; // To use the MediaFile entity.
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/instructors")
@Validated
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private LessonService lessonService;
    @Autowired
    private MediaFileRepository mediaFileRepository; // Autowire the MediaFileRepository
    // Get all instructors
    @GetMapping
    public ResponseEntity<List<Instructor>> getAllInstructors() {
        return ResponseEntity.ok(instructorService.getAllInstructors());
    }

    @GetMapping("/lessons/{lessonId}/attendance")
    public ResponseEntity<List<Student>> getAttendance(@PathVariable Long lessonId) {
        Lesson lesson = lessonService.getLessonById(lessonId);
        return ResponseEntity.ok(lesson.getAttendance());
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
    // Tested final
    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@AuthenticationPrincipal User user, @RequestBody CourseDTO courseDTO) {
        return ResponseEntity.ok(instructorService.createCourse(user.getId(), courseDTO));
    }

    // Tested final
    @PutMapping("/courses/{courseId}")
    public ResponseEntity<Course> updateCourse(@AuthenticationPrincipal User user,
                                               @PathVariable Long courseId,
                                               @RequestBody CourseDTO updatedCourse) {
        Course course = instructorService.updateCourse(user.getId(), courseId, updatedCourse);
        if (course != null) {
            return ResponseEntity.ok(course);
        }
        return ResponseEntity.notFound().build();
    }

    // Tested final
    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Void> deleteCourse(@AuthenticationPrincipal User user,
                                             @PathVariable Long courseId) {
        instructorService.deleteCourse(user.getId(), courseId);
        return ResponseEntity.noContent().build();
    }

    // Tested final
    @PostMapping("/courses/{courseId}/lessons")
    public ResponseEntity<Course> addLessonToCourse(
            @AuthenticationPrincipal User user,
            @PathVariable Long courseId,
            @RequestBody LessonDTO lesson) {
        return ResponseEntity.ok(instructorService.addLessonToCourse(user.getId(), courseId, lesson));
    }

    // Tested final
    @PostMapping("/courses/{courseId}/questionBank")
    public ResponseEntity<QuestionBank> addQuestionToBank(
            @AuthenticationPrincipal User user,
            @PathVariable Long courseId,
            @Valid @RequestBody QuestionDTO questionDTO) {
        return ResponseEntity.ok(instructorService.addQuestionToBank(user.getId(), courseId, questionDTO));
    }


    // Tested final
    @PostMapping("/courses/{courseId}/quiz")
    public ResponseEntity<Quiz> addQuizToCourse(
            @AuthenticationPrincipal User user,
            @PathVariable Long courseId,
            @Valid @RequestBody QuizDTO quizDTO) {
        return ResponseEntity.ok(instructorService.createQuiz(user.getId(), courseId, quizDTO));
    }

    // Tested final
    @DeleteMapping("/courses/{courseId}/students/{studentId}")
    public ResponseEntity<Void> removeStudentFromCourse(@AuthenticationPrincipal User user,
                                                        @PathVariable Long courseId,
                                                        @PathVariable Long studentId){
        instructorService.removeStudentFromCourse(user.getId(), courseId, studentId);
        return ResponseEntity.noContent().build();
    }

    // Tested final
    @PutMapping("/edit")
    public ResponseEntity<Instructor> editInstructorProfile(@AuthenticationPrincipal User user, @RequestBody InstructorDTO instructorDTO) {
        return ResponseEntity.ok(instructorService.updateInstructorProfile(user.getId(), instructorDTO));
    }


    // Tested final
    @PostMapping("/courses/{courseId}/media/upload")
    public ResponseEntity<String> uploadMediaToCourse(
            @AuthenticationPrincipal User user,
            @PathVariable Long courseId,
            @RequestParam("filePath") String filePath) {

        try {
            instructorService.saveMediaFile(user.getId(), courseId, filePath);

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

    // Tested final
    @PostMapping("/courses/{courseId}/assignments")
    public ResponseEntity<Course> addAssignmentToCourse(@AuthenticationPrincipal User user,
                                                   @PathVariable Long courseId,
                                                   @Valid @RequestBody AssignmentDTO assignmentDTO) {
        return ResponseEntity.ok(instructorService.addAssignmentToCourse(user.getId(), courseId, assignmentDTO));
    }

    // Tested final
    @PostMapping("/grade/{assignmentId}/{studentId}")
    public ResponseEntity<String> gradeAssignment(@AuthenticationPrincipal User user,
                                                  @PathVariable Long studentId,
                                                  @RequestParam int grade) {
        return instructorService.gradeAssignment(user.getId(), studentId, grade);
    }
}
