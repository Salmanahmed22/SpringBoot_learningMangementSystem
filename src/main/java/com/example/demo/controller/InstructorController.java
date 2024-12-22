package com.example.demo.controller;

import com.example.demo.models.Course;
import com.example.demo.models.Instructor;
import com.example.demo.models.Lesson;
//import com.example.demo.models.Notification;
import com.example.demo.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

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

    // Delete an instructor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Long id) {
        instructorService.deleteInstructor(id);
        return ResponseEntity.noContent().build();
    }

//    //notification
////    @PutMapping("/{id}/notifications/mark-as-read")
////    public ResponseEntity<Void> markNotificationsAsRead(@PathVariable Long id) {
////        instructorService.markNotificationsAsRead(id);
////        return ResponseEntity.ok().build();
////    }
//
////    @GetMapping("/{id}/notifications")
////    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long id, @RequestParam(required = false) Boolean unread) {
////        List<Notification> notifications = instructorService.getNotifications(id, unread);
////        return ResponseEntity.ok(notifications);
////    }
//
    // Create a new course
    @PostMapping("/{id}/courses")
    public ResponseEntity<Course> createCourse(@PathVariable Long id, @RequestBody Course course) {
        return ResponseEntity.ok(instructorService.createCourse(course, id));
    }

    // Update a course
    @PutMapping("/{instructorId}/courses/{courseId}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long instructorId,
                                               @PathVariable Long courseId,
                                               @RequestBody Course updatedCourse) {
        Course course = instructorService.updateCourse(courseId, updatedCourse);
        if (course != null) {
            return ResponseEntity.ok(course);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete a course
    @DeleteMapping("/{instructorId}/courses/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long instructorId,
                                             @PathVariable Long courseId) {
        instructorService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }

    //Add a lesson to course
    @PostMapping("/{instructorId}/courses/{courseId}/lessons")
    public ResponseEntity<Lesson> addLessonToCourse(
            @PathVariable Long instructorId,
            @PathVariable Long courseId,
            @RequestBody Lesson lesson) {
        return ResponseEntity.ok(instructorService.addLessonToCourse(instructorId, courseId, lesson));
    }

//    //Remove student from course
//    @DeleteMapping("/{instructorId}/courses/{courseId}/{studentId}")
//    public ResponseEntity<Void> removeStudentFromCourse(@PathVariable Long instructorId,
//                                                        @PathVariable Lo
//                                                        +ng courseId,
//                                                        @PathVariable Long studentId){
//        instructorService.removeStudentFromCourse(instructorId, courseId, studentId);
//        return ResponseEntity.noContent().build();
//    }
}
