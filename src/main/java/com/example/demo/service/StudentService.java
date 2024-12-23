package com.example.demo.service;

import com.example.demo.dtos.AnswerDTO;
import com.example.demo.dtos.StudentDTO;
import com.example.demo.dtos.SubmissionDTO;
import com.example.demo.models.*;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.InstructorRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private NotificationService notificationService;

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

        Course course = courseService.getCourseById(courseId);

        if (student.getLevel() < course.getMinLevel()) {
            throw new IllegalArgumentException("Student level is not sufficient to enroll in this course");
        }

        List<Course> enrolledCourses = student.getEnrolledCourses();
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
            student.setEnrolledCourses(enrolledCourses);

            studentRepository.save(student);
            // Notify the student
            String messageStud = "You have successfully enrolled in the course: " + course.getTitle();
            Notification notification = new Notification(messageStud, student);
            notificationRepository.save(notification);

            // Notify the instructor
            Instructor instructor = course.getInstructor();

            if (instructor != null) {
                String instructorMessage = "Student " + student.getName() + " (ID: " + student.getId() + ") has enrolled in your course: " + course.getTitle();
                Notification instructorNotification = new Notification(instructorMessage, instructor);
                notificationRepository.save(instructorNotification);
            } else {
                throw new IllegalArgumentException("Instructor not found for this course");
            }

            return student;
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
    public double takeQuiz(Long studentId, Long quizId, SubmissionDTO submissionDTO) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        List<Course> enrolledCourses = student.getEnrolledCourses();
        for (Course course : enrolledCourses) {
            List<Quiz> quizzes = course.getQuizzes();
            for (Quiz quiz : quizzes) {
                if (quiz.getId().equals(quizId)) {
                    if ((quiz.getQuizDate().plus(quiz.getDuration())).isBefore(LocalDateTime.now())) {
                        throw new IllegalArgumentException("Quiz deadline has passed.");
                    }
                    Submission submission = new Submission();
                    submission.setStudent(student);
                    List<Answer> answers = new ArrayList<>();
                    for(AnswerDTO answerDTO : submissionDTO.getAnswers()) {
                        Answer answer = new Answer();
                        answer.setId(answerDTO.getId());
                        answer.setAnswer(answerDTO.getAnswer());
                        answers.add(answer);
                    }
                    submission.setAnswers(answers);
                    return quizService.submitQuiz(quizId, submission);
                }
            }
        }
        throw new IllegalArgumentException("Quiz not found in enrolled courses.");
    }

    public List<Notification> getNotifications(Long studentId, Boolean unread) {
        // Delegate to NotificationService
        return notificationService.getNotifications(studentId, unread);
    }

    public Student updateStudentProfile(Long id, StudentDTO studentDTO) {
        Student student = studentRepository.findById(id).orElse(null);

        if (student != null) {
            if (studentDTO.getName() != null)
                student.setName(studentDTO.getName());
            if (studentDTO.getLevel() != 0)
                student.setLevel(studentDTO.getLevel());
            if (studentDTO.getEmail() != null)
                student.setEmail(studentDTO.getEmail());
            if (studentDTO.getPassword() != null){
                student.setPassword(bCryptPasswordEncoder.encode(studentDTO.getPassword()));
            }

            return studentRepository.save(student);
        }
        throw new IllegalArgumentException("Student not found");
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
