package com.example.demo.service;

import com.example.demo.DTO.CourseRequest;
import com.example.demo.models.Course;
import com.example.demo.models.Instructor;
import com.example.demo.models.Lesson;
//import com.example.demo.models.Notification;
import com.example.demo.models.Student;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.InstructorRepository;
//import com.example.demo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private StudentService studentService;

    // Get all instructors
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    // Get an instructor by ID
    public Instructor getInstructorById(Long id) {
        Optional<Instructor> instructor = instructorRepository.findById(id);
        return instructor.orElse(null);
    }

    // Create a new instructor
    public Instructor createInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    // Update an instructor
    public Instructor updateInstructor(Long id, Instructor updatedInstructor) {
        Optional<Instructor> existingInstructor = instructorRepository.findById(id);
        if (existingInstructor.isPresent()) {
            Instructor instructor = existingInstructor.get();
            instructor.setName(updatedInstructor.getName());
            instructor.setEmail(updatedInstructor.getEmail());
            instructor.setPassword(updatedInstructor.getPassword());
            instructor.setDepartment(updatedInstructor.getDepartment());
            instructor.setEmployeeId(updatedInstructor.getEmployeeId());
            return instructorRepository.save(instructor);
        }
        return null;
    }

    // Delete an instructor
    public void deleteInstructor(Long id) {
        instructorRepository.deleteById(id);
    }


//    @Autowired
//    private NotificationRepository notificationRepository;
//
//    public List<Notification> getNotifications(Long studentId, Boolean unread) {
//        if (unread != null && unread) {
//            return notificationRepository.findByUserIdAndIsRead(studentId, false);
//        }
//        return notificationRepository.findByUserId(studentId);
//    }
//
//    public void markNotificationsAsRead(Long studentId) {
//        List<Notification> notifications = notificationRepository.findByUserIdAndIsRead(studentId, false);
//        for (Notification notification : notifications) {
//            notification.setRead(true);
//        }
//        notificationRepository.saveAll(notifications);
//    }

    // Create a new course
    public Course createCourse(CourseRequest courseRequest) {
        Instructor instructor = getInstructorById(courseRequest.getInstructorId());
        Course course = courseService.createCourse(courseRequest);
        List<Course> courses = instructor.getCourses();
        courses.add(course);
        instructor.setCourses(courses);
        courseRepository.save(course);
        return course;
    }

    // Update a course
    public Course updateCourse(Long id, CourseRequest updatedCourse) {
        return courseService.updateCourse(id, updatedCourse);
    }

    // Delete an instructor
    public void deleteCourse(Long instructorid, Long courseId) {
        Instructor instructor = getInstructorById(instructorid);
        Course course = courseService.getCourseById(courseId);
        List<Course> courses = instructor.getCourses();
        courses.remove(course);
        instructor.setCourses(courses);
        instructorRepository.save(instructor);
        courseService.deleteCourse(courseId);
    }

    // Add a lesson to course
    public Lesson addLessonToCourse(Long instructorId, Long courseId, Lesson lesson) {

        Instructor instructor = instructorRepository.findById(instructorId).orElse(null);

        Course course = courseService.getCourseById(courseId);

        if (!course.getInstructor().equals(instructor)) {
            throw new IllegalArgumentException("Course does not belong to the instructor");
        }

        courseService.addLesson(courseId, lesson);
        return lesson;
    }

    public void removeStudentFromCourse(Long instructorId, Long courseId, Long studentId) {

        Student student = studentService.getStudentById(studentId);

        Instructor instructor = instructorRepository.findById(instructorId).orElse(null);

        Course course = courseService.getCourseById(courseId);

        if (!course.getInstructor().equals(instructor)) {
            throw new IllegalArgumentException("Course does not belong to the instructor");
        }
        courseService.removeStudentFromCourse(course, student);

    }
}
