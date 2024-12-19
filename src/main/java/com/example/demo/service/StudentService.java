package com.example.demo.service;

import com.example.demo.models.Course;
import com.example.demo.models.Lesson;
//import com.example.demo.models.Notification;
//import com.example.demo.repository.NotificationRepository;
import com.example.demo.models.Student;
//import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    private CourseRepository courseRepository;

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student enrollCourse(Long studentId, Course course) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student != null) {
            student.enrollCourse(course);
            return studentRepository.save(student);
        }
        return null;
    }

    public List<Course> getEnrolledCourses(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        return student.getEnrolledCourses();
    }

    public List<Course> getAvailableCourses(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        List<Course> allCourses = courseRepository.findAll();

        return student.viewAvailableCourses(allCourses);
    }

    public List<Lesson> viewCourseLessons(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        return student.viewCourseLessons(course);
    }

    public Student unenrollCourse(Long studentId, Course course) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student != null) {
            student.unenrollCourse(course);
            return studentRepository.save(student);
        }
        return null;
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
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

}
