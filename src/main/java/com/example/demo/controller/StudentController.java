package com.example.demo.controller;

import com.example.demo.models.Course;
import com.example.demo.models.Lesson;
import com.example.demo.models.Quiz;
import com.example.demo.models.Notification;
import com.example.demo.models.Student;
import com.example.demo.models.Assignment;
import com.example.demo.models.Submission;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class  StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.createStudent(student));
    }

    //done test
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    //done test
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    //done test
    @PutMapping("/{studentId}/enroll/{courseId}")
    public ResponseEntity<Student> enrollCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        return ResponseEntity.ok(studentService.enrollCourse(studentId, courseId));
    }


    @GetMapping("/{studentId}/available-courses")
    public List<Course> viewAvailableCourses(@PathVariable Long studentId) {
        return studentService.getAvailableCourses(studentId);
    }

    @GetMapping("/{studentId}/enrolled-courses")
    public List<Course> viewEnrolledCourses(@PathVariable Long studentId) {
        return studentService.getEnrolledCourses(studentId);
    }

    @GetMapping("/{studentId}/courses/{courseId}/lessons")
    public ResponseEntity<List<Lesson>> viewCourseLessons(@PathVariable Long studentId, @PathVariable Long courseId) {
        List<Lesson> lessons = studentService.getCourseLessons(studentId, courseId);
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/{studentId}/courses/{courseId}/lessons/{lessonId}")
    public ResponseEntity<String> viewLessonContent(@PathVariable Long studentId, @PathVariable Long courseId, @PathVariable Long lessonId) {
        String lessonContent = studentService.getLessonContent(studentId, courseId, lessonId);
        return ResponseEntity.ok(lessonContent);
    }

    @GetMapping("/{studentId}/courses/{courseId}/assignments")
    public ResponseEntity<List<Assignment>> viewAssignments(
            @PathVariable Long studentId, @PathVariable Long courseId) {
        List<Assignment> assignments = studentService.getAssignments(studentId, courseId);
        return ResponseEntity.ok(assignments);
    }

    @PostMapping("/{studentId}/assignments/{assignmentId}/submit")
    public ResponseEntity<String> submitAssignment(@PathVariable Long studentId, @PathVariable Long assignmentId, @RequestBody SubmissionRequest submissionRequest) {
        try {
            studentService.submitAssignment(studentId, assignmentId, submissionRequest.getSubmissionContent());
            return ResponseEntity.ok("Assignment submitted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DTO class for submission content
    public static class SubmissionRequest {
        private String submissionContent;

        public String getSubmissionContent() {
            return submissionContent;
        }

        public void setSubmissionContent(String submissionContent) {
            this.submissionContent = submissionContent;
        }
    }


    @GetMapping("/{studentId}/courses/{courseId}/quizzes")
    public ResponseEntity<List<Quiz>> viewQuizzes(@PathVariable Long studentId, @PathVariable Long courseId) {
        List<Quiz> quizzes = studentService.getQuizzes(studentId, courseId);
        return ResponseEntity.ok(quizzes);
    }


    @PostMapping("/{studentId}/quizzes/{quizId}/submit")
    public ResponseEntity<String> takeQuiz(@PathVariable Long studentId, @PathVariable Long quizId, @RequestBody Submission submission) {
        try {
            studentService.takeQuiz(studentId, quizId, submission);
            return ResponseEntity.ok("Quiz submission successful.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/notifications")
    public ResponseEntity<List<Notification>> viewNotifications(@PathVariable Long id, @RequestParam(required = false) Boolean unread) {
        List<Notification> notifications = studentService.getNotifications(id, unread);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}/notifications/mark-as-read")
    public ResponseEntity<Void> markNotificationsAsRead(@PathVariable Long id) {
        studentService.markNotificationsAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/edit")
    public ResponseEntity<Student> setStudentLevel(@PathVariable Long id, @RequestBody short level) {
        return ResponseEntity.ok(studentService.updateStudentLevel(id, level));
    }

    //done test
    @PutMapping("/{studentId}/unroll/{courseId}")
    public ResponseEntity<Student> unrollCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        return ResponseEntity.ok(studentService.unrollCourse(studentId, courseId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

}
