package com.example.demo.controller;

import com.example.demo.models.Course;
import com.example.demo.models.Lesson;
import com.example.demo.models.Quiz;
import com.example.demo.models.Notification;
import com.example.demo.models.Student;
import com.example.demo.models.Assignment;
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

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {

        return ResponseEntity.ok(studentService.getAllStudents());
    }


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

    @GetMapping("/{studentId}/courses/{courseId}/quizzes")
    public ResponseEntity<List<Quiz>> viewQuizzes(
            @PathVariable Long studentId, @PathVariable Long courseId) {
        List<Quiz> quizzes = studentService.getQuizzes(studentId, courseId);
        return ResponseEntity.ok(quizzes);
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
