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

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }


    public Student enrollCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        Course course = courseRepository.findById(courseId).orElse(null);
        if (student != null && course != null) {
            if (student.getLevel() < course.getMinLevel()) {
                throw new IllegalArgumentException("Student level is not sufficient to enroll in this course");
            }
            List<Course> enrolledCourses = student.getEnrolledCourses();
            enrolledCourses.add(course);
            student.setEnrolledCourses(enrolledCourses);
            return studentRepository.save(student);
        }
        return null;
    }

    public List<Course> getAvailableCourses(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        List<Course> allCourses = courseRepository.findAll();

        return allCourses.stream()
                .filter(course -> student.getEnrolledCourses().contains(course) && course.getMinLevel() <= student.getLevel())
                .collect(Collectors.toList());
    }

    public List<Course> getEnrolledCourses(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        return student.getEnrolledCourses();
    }

    public List<Lesson> getCourseLessons(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (!student.getEnrolledCourses().contains(course)) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }

        return course.getLessons();
    }

    public String getLessonContent(Long studentId, Long courseId, Long lessonId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        List<Course> enrolledCourses = student.getEnrolledCourses();
        for (Course course : enrolledCourses) {
            if (course.getId().equals(courseId)) {
                List<Lesson> lessons = course.getLessons();
                for (Lesson lesson : lessons) {
                    if (lesson.getId().equals(lessonId)) {
                        return lesson.getContent();
                    }
                }
            }
        }
        throw new IllegalArgumentException("Lesson not found in enrolled courses");
    }

    public List<Assignment> getAssignments(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        List<Course> enrolledCourses = student.getEnrolledCourses();
        for (Course course : enrolledCourses) {
            if (course.getId().equals(courseId)) {
                return course.getAssignments();
            }
        }
        throw new IllegalArgumentException("Student is not enrolled in the course with ID: " + courseId);
    }

    public List<Quiz> getQuizzes(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        List<Course> enrolledCourses = student.getEnrolledCourses();
        for (Course course : enrolledCourses) {
            if (course.getId().equals(courseId)) {
                return course.getQuizzes();
            }
        }
        // better version we can make sure also if the course exist if not exist throw error
        throw new IllegalArgumentException("Student is not enrolled in this course");
    }

    public List<Notification> getNotifications(Long studentId, Boolean unread) {
        if (unread != null && unread) {
            return notificationRepository.findByUserIdAndIsRead(studentId, false);
        }
        return notificationRepository.findByUserId(studentId);
    }

    public void markNotificationsAsRead(Long studentId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsRead(studentId, false);
        for (Notification notification : notifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(notifications);
    }

    public Student unrollCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        Course course = courseRepository.findById(courseId).orElse(null);

        if (student != null) {
            List<Course> enrolledCourses = student.getEnrolledCourses();
            for(Course enrolledCourse: enrolledCourses){
                if(enrolledCourse.getId().equals(courseId)){
                    List<Course> newEnrolledCourses = student.getEnrolledCourses();
                    newEnrolledCourses.remove(course);
                    student.setEnrolledCourses(newEnrolledCourses);
                    return studentRepository.save(student);
                }
            }
            throw new IllegalArgumentException("Student is not enrolled in this course");

        }
        else {
            throw new IllegalArgumentException("Student not found");
        }
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }


}
