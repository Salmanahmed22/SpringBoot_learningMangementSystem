package com.example.demo.service;

import com.example.demo.models.Course;
import com.example.demo.models.Lesson;
import com.example.demo.models.Quiz;
import com.example.demo.models.Assignment;
import com.example.demo.models.Notification;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.models.Student;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    public Student getStudentById(String id) {
        return studentRepository.findById(Long.valueOf(id)).orElse(null);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student enrollCourse(String studentId, String courseId) {
        Student student = studentRepository.findById(Long.valueOf(studentId)).orElse(null);
        Course course = courseRepository.findById(Long.valueOf(courseId)).orElse(null);
        if (student != null && course != null) {
            if (student.getLevel() < course.getMinLevel()) {
                throw new IllegalArgumentException("Student level is not sufficient to enroll in this course");
            }
            List<Course> enrolledCourses = student.getEnrolledCourses();
            enrolledCourses.add(course);
            return studentRepository.save(student);
        }
        return null;
    }

    public List<Course> viewAvailableCourses(String studentId) {
        Student student = studentRepository.findById(Long.valueOf(studentId))
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        List<Course> allCourses = courseRepository.findAll();

        return allCourses.stream()
                .filter(course -> student.getEnrolledCourses().contains(course) && course.getMinLevel() <= student.getLevel())
                .collect(Collectors.toList());
    }

    public List<Course> getEnrolledCourses(String studentId) {
        Student student = studentRepository.findById(Long.valueOf(studentId))
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        return student.getEnrolledCourses();
    }

    public List<Lesson> viewCourseLessons(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (!student.getEnrolledCourses().contains(course)) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }

        return course.getLessons();
    }

    public List<Assignment> getAssignments(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        return student.viewAssignments(courseId);
    }

    public List<Quiz> getQuizzes(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        return student.viewQuizzes(courseId);
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



    public List<Notification> getNotifications(Long studentId, Boolean unread) {
        if (unread != null && unread) {
            return notificationRepository.findByUserIdAndIsRead(String.valueOf(studentId), false);
        }
        return notificationRepository.findByUserId(String.valueOf(studentId));
    }

    public void markNotificationsAsRead(Long studentId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsRead(String.valueOf(studentId), false);
        for (Notification notification : notifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(notifications);
    }

}
