package com.example.demo.controller;

import com.example.demo.dtos.AssignmentSubmissionDTO;
import com.example.demo.dtos.StudentDTO;
import com.example.demo.dtos.SubmissionDTO;
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

    // tested
    @PutMapping("/{studentId}/enroll/{courseId}")
    public ResponseEntity<Student> enrollCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        return ResponseEntity.ok(studentService.enrollCourse(studentId, courseId));
    }

    // tested
    @GetMapping("/{studentId}/available-courses")
    public List<Course> viewAvailableCourses(@PathVariable Long studentId) {
        return studentService.getAvailableCourses(studentId);
    }

    // tested
    @GetMapping("/{studentId}/enrolled-courses")
    public List<Course> viewEnrolledCourses(@PathVariable Long studentId) {
        return studentService.getEnrolledCourses(studentId);
    }

    // tested
    @GetMapping("/{studentId}/courses/{courseId}/lessons")
    public ResponseEntity<List<Lesson>> viewCourseLessons(@PathVariable Long studentId, @PathVariable Long courseId) {
        List<Lesson> lessons = studentService.getCourseLessons(studentId, courseId);
        return ResponseEntity.ok(lessons);
    }

    // tested
    @GetMapping("/{studentId}/courses/{courseId}/lessons/{lessonId}")
    public ResponseEntity<String> viewLessonContent(@PathVariable Long studentId, @PathVariable Long courseId, @PathVariable Long lessonId) {
        String lessonContent = studentService.getLessonContent(studentId, courseId, lessonId);
        return ResponseEntity.ok(lessonContent);
    }

    // tested
    @GetMapping("/{studentId}/courses/{courseId}/assignments")
    public ResponseEntity<List<Assignment>> viewAssignments(
            @PathVariable Long studentId, @PathVariable Long courseId) {
        List<Assignment> assignments = studentService.getAssignments(studentId, courseId);
        return ResponseEntity.ok(assignments);
    }

    @PostMapping("/{studentId}/assignments/{assignmentId}/submit")
    public ResponseEntity<String> submitAssignment(@PathVariable Long studentId, @PathVariable Long assignmentId, @RequestBody AssignmentSubmissionDTO assignmentSubmissionDTO) {
        try {
            studentService.submitAssignment(studentId, assignmentId, assignmentSubmissionDTO.getSubmissionContent());
            return ResponseEntity.ok("Assignment submitted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // tested
    @GetMapping("/{studentId}/courses/{courseId}/quizzes")
    public ResponseEntity<List<Quiz>> viewQuizzes(@PathVariable Long studentId, @PathVariable Long courseId) {
        List<Quiz> quizzes = studentService.getQuizzes(studentId, courseId);
        return ResponseEntity.ok(quizzes);
    }

    //
    @PostMapping("/{studentId}/quizzes/{quizId}/submit")
    public ResponseEntity<String> submitQuiz(@PathVariable Long studentId, @PathVariable Long quizId, @RequestBody SubmissionDTO submissionDTO) {
        try {
            return ResponseEntity.ok("Grade " + studentService.takeQuiz(studentId, quizId, submissionDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/{studentId}/notifications")
    public ResponseEntity<List<Notification>> viewNotifications(@PathVariable Long studentId, @RequestParam(required = false) Boolean unread) {
        return ResponseEntity.ok(studentService.getNotifications(studentId, unread));
    }

    //done test
    @PutMapping("{id}/editProfile")
    public ResponseEntity<Student> editStudentProfile(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {
        return ResponseEntity.ok(studentService.updateStudentProfile(id, studentDTO));
    }

    //done test
    @PutMapping("/{studentId}/unroll/{courseId}")
    public ResponseEntity<Student> unrollCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        return ResponseEntity.ok(studentService.unrollCourse(studentId, courseId));
    }

    //move it to admin as delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }


}
