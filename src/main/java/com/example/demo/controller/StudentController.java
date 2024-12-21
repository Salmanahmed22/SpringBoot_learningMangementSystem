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

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable String id) {
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

    @PutMapping("/{studentId}/enroll/{courseId}")
    public ResponseEntity<Student> enrollCourse(@PathVariable String studentId, @PathVariable String courseId) {
        return ResponseEntity.ok(studentService.enrollCourse(studentId, courseId));
    }

    @GetMapping("/{studentId}/available-courses")
    public List<Course> viewAvailableCourses(@PathVariable String studentId) {
        return studentService.viewAvailableCourses(studentId);
    }

    @GetMapping("/{studentId}/enrolled-courses")
    public List<Course> getEnrolledCourses(@PathVariable String studentId) {
        return studentService.getEnrolledCourses(studentId);
    }

    @GetMapping("/{studentId}/courses/{courseId}/lessons")
    public ResponseEntity<List<Lesson>> viewCourseLessons(@PathVariable String studentId, @PathVariable String courseId) {
        List<Lesson> lessons = studentService.viewCourseLessons(studentId, courseId);
        return ResponseEntity.ok(lessons);
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

    @PutMapping("/{id}/unenroll")
    public ResponseEntity<Student> unenrollCourse(@PathVariable Long id, @RequestBody Course course) {
        return ResponseEntity.ok(studentService.unenrollCourse(id, course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/notifications/mark-as-read")
    public ResponseEntity<Void> markNotificationsAsRead(@PathVariable Long id) {
        studentService.markNotificationsAsRead(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/notifications")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long id, @RequestParam(required = false) Boolean unread) {
        List<Notification> notifications = studentService.getNotifications(id, unread);
        return ResponseEntity.ok(notifications);
    }

}
