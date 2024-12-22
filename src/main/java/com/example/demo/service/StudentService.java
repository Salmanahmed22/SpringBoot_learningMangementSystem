package com.example.demo.service;

import com.example.demo.models.*;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.service.QuizService;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private QuizService quizService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private AssignmentService assignmentService;

    // tested
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    // tested
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    // tested
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // tested
    public Student enrollCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            throw new IllegalArgumentException("Student not found");
        }

        Course course = courseService.getCourseById(courseId); // Ensure it's a managed entity

        if (student.getLevel() < course.getMinLevel()) {
            throw new IllegalArgumentException("Student level is not sufficient to enroll in this course");
        }

        List<Course> enrolledCourses = student.getEnrolledCourses();
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
            student.setEnrolledCourses(enrolledCourses);
            return studentRepository.save(student);
        } else {
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }
    }

    // tested
    public List<Course> getAvailableCourses(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        List<Course> allCourses = courseService.getAllCourses();

        return allCourses.stream()
                .filter(course -> student.getEnrolledCourses().contains(course) && course.getMinLevel() <= student.getLevel())
                .collect(Collectors.toList());
    }

    // tested
    public List<Course> getEnrolledCourses(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        return student.getEnrolledCourses();
    }

    // tested
    public List<Lesson> getCourseLessons(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Course course = courseService.getCourseById(courseId);

        if (!student.getEnrolledCourses().contains(course) && course != null) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }

        return course.getLessons();
    }

    // tested
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

    // tested and corrected
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

    // tested and corrected
    // Submission is string which is not the best to do but for now
    public void submitAssignment(Long studentId,Long assignmentId, String submissionContent) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        List<Course> enrolledCourses = student.getEnrolledCourses();
        Assignment assignment = null;
        for (Course course : enrolledCourses) {
            List<Assignment> courseAssignments = course.getAssignments();
            for (Assignment a : courseAssignments) {
                if (a.getId().equals(assignmentId)) {
                    assignment = a;
                    break;
                }
            }
        }
        if (assignment == null) {
            throw new IllegalArgumentException("Assignment not found in enrolled courses");
        }

        LocalDateTime currentDate = LocalDateTime.now();
        if (currentDate.isAfter(assignment.getDueDate())) {
            throw new IllegalArgumentException("Cannot submit assignment after the due date");
        }

        assignmentService.submitAssignment(assignment.getId(), studentId, submissionContent);
    }

    // tested
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

    // unsupported media problem
    public void takeQuiz(Long studentId, Long quizId, Submission submission) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        List<Course> enrolledCourses = student.getEnrolledCourses();
        for (Course course : enrolledCourses) {
            List<Quiz> quizzes = course.getQuizzes();
            for (Quiz quiz : quizzes) {
                if (quiz.getId().equals(quizId)) {
                    if (quiz.getDeadline().isBefore(LocalDateTime.now())) {
                        throw new IllegalArgumentException("Quiz deadline has passed.");
                    }
                    quizService.submitQuiz(quizId, submission);
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Quiz not found in enrolled courses.");
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

    public Student updateStudentLevel(Long id, short level) {
        Student student = studentRepository.findById(id).orElse(null);

        if (student != null) {
            student.setLevel(level);
            return studentRepository.save(student);
        }
        return null;
    }

    public Student unrollCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        Course course = courseService.getCourseById(courseId);

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

    public void removeEnrolledCourse(Course course, Student student) {
        student.getEnrolledCourses().remove(course);
        studentRepository.save(student);
    }
}
