package com.example.demo.controller;

import com.example.demo.dtos.AssignmentSubmissionDTO;
import com.example.demo.dtos.StudentDTO;
import com.example.demo.dtos.SubmissionDTO;
import com.example.demo.models.*;
import com.example.demo.service.StudentService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class  StudentController {

    @Autowired
    private StudentService studentService;


    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.createStudent(student));
    }

    // tested
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    // tested
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // tested final
    @PutMapping("/{studentId}/enroll/{courseId}")
    public ResponseEntity<Student> enrollCourse(@AuthenticationPrincipal User user, @PathVariable Long courseId) {
        return ResponseEntity.ok(studentService.enrollCourse(user.getId(), courseId));
    }

    // tested
    @GetMapping("/available-courses")
    public List<Course> viewAvailableCourses(@AuthenticationPrincipal User user) {
        return studentService.getAvailableCourses(user.getId());
    }

    // tested
    @GetMapping("/enrolled-courses")
    public List<Course> viewEnrolledCourses(@AuthenticationPrincipal User user) {
        return studentService.getEnrolledCourses(user.getId());
    }

    // tested
    @GetMapping("/courses/{courseId}/lessons")
    public ResponseEntity<List<Lesson>> viewCourseLessons(@AuthenticationPrincipal User user, @PathVariable Long courseId) {
        List<Lesson> lessons = studentService.getCourseLessons(user.getId(), courseId);
        return ResponseEntity.ok(lessons);
    }

    // tested
    @GetMapping("/courses/{courseId}/lessons/{lessonId}")
    // pass the OTP ?XXXXXX (request param)
    public ResponseEntity<String> viewLessonContent(@AuthenticationPrincipal User user, @PathVariable Long courseId, @PathVariable Long lessonId ,@RequestParam String enterdOTP) {
        String lessonContent = studentService.getLessonContent(user.getId(), courseId, lessonId , enterdOTP);
        return ResponseEntity.ok(lessonContent);
    }

    // tested
    @GetMapping("/courses/{courseId}/assignments")
    public ResponseEntity<List<Assignment>> viewAssignments(
            @AuthenticationPrincipal User user, @PathVariable Long courseId) {
        List<Assignment> assignments = studentService.getAssignments(user.getId(), courseId);
        return ResponseEntity.ok(assignments);
    }

    // Tested final
    @PostMapping("/assignments/{assignmentId}/submit")
    public ResponseEntity<String> submitAssignment(@AuthenticationPrincipal User user,
                                                   @PathVariable Long assignmentId,
                                                   @RequestBody AssignmentSubmissionDTO assignmentSubmissionDTO) {
        try {
            studentService.submitAssignment(user.getId(), assignmentId, assignmentSubmissionDTO.getSubmissionContent());
            return ResponseEntity.ok("Assignment submitted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // tested
    @GetMapping("/courses/{courseId}/quizzes")
    public ResponseEntity<List<Quiz>> viewQuizzes(@AuthenticationPrincipal User user, @PathVariable Long courseId) {
        List<Quiz> quizzes = studentService.getQuizzes(user.getId(), courseId);
        return ResponseEntity.ok(quizzes);
    }

    //
    @PostMapping("/quizzes/{quizId}/submit")
        public ResponseEntity<String> submitQuiz(@AuthenticationPrincipal User user,
                                                 @PathVariable Long quizId,
                                                 @RequestBody SubmissionDTO submissionDTO) {
        try {
            return ResponseEntity.ok("Grade " + studentService.takeQuiz(user.getId(), quizId, submissionDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> viewNotifications(@AuthenticationPrincipal User user, @RequestParam(required = false) Boolean unread) {
        return ResponseEntity.ok(studentService.getNotifications(user.getId(), unread));
    }

    //done test
    @PutMapping("/editProfile")
    public ResponseEntity<Student> editStudentProfile(@AuthenticationPrincipal User user, @RequestBody StudentDTO studentDTO) {
        return ResponseEntity.ok(studentService.updateStudentProfile(user.getId(), studentDTO));
    }

    // Tested final
    @PutMapping("/unroll/{courseId}")
    public ResponseEntity<Student> unrollCourse(@AuthenticationPrincipal User user, @PathVariable Long courseId) {
        return ResponseEntity.ok(studentService.unrollCourse(user.getId(), courseId));
    }

    //move it to admin as delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/assignmentsGrades")
    public ResponseEntity<Map<Long,String>> viewAssignmentsGrades(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(studentService.viewAssignmentsGrades(user.getId()));
    }

    @GetMapping("/quizzesGrades")
    public ResponseEntity<Map<Long,String>> viewQuizzesGrades(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(studentService.viewQuizzesGrades(user.getId()));
    }



}
