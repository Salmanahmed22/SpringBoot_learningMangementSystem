package com.example.demo.service;

import com.example.demo.dtos.*;
import com.example.demo.models.*;
//import com.example.demo.models.Notification;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.MediaFileRepository;
import com.example.demo.repository.InstructorRepository;
//import com.example.demo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private QuestionBankService questionBankService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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


    // Fetch notifications for the instructor
    public List<Notification> getNotifications(Long instructorId, Boolean unread) {
        return notificationService.getNotifications(instructorId, unread);
    }

    public Course createCourse(Long instructorId, CourseDTO courseDTO) {
        Instructor instructor = getInstructorById(instructorId);
        if(instructor == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Instructor not found");
        }
        Course course = courseService.createCourse(instructor, courseDTO);
        List<Course> courses = instructor.getCourses();
        courses.add(course);
        instructor.setCourses(courses);
        instructorRepository.save(instructor);
        return course;
    }

    public Course updateCourse(Long instructorId, Long courseId, CourseDTO updatedCourse) {
        Course course = courseService.getCourseById(courseId);
        Instructor instructor = getInstructorById(instructorId);
        if (!course.getInstructor().equals(instructor)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Instructor has no authority on this course");
        }

        // Notify all enrolled students about the course update
        List<Student> enrolledStudents = course.getEnrolledStudents();
        String message = "The course '" + course.getTitle() + "' has been updated by instructor: " + instructor.getName() + ".";
        for (Student student : enrolledStudents) {
            notificationService.createNotification(student.getId(), message);
        }

        return courseService.updateCourse(courseId, updatedCourse);
    }

    public void deleteCourse(Long instructorid, Long courseId) {
        Instructor instructor = getInstructorById(instructorid);
        Course course = courseService.getCourseById(courseId);
        List<Course> courses = instructor.getCourses();
        courses.remove(course);
        instructor.setCourses(courses);
        instructorRepository.save(instructor);
        courseService.deleteCourse(courseId);
    }

    public Course addLessonToCourse(Long instructorId, Long courseId, LessonDTO lessonDTO) {

        Instructor instructor = instructorRepository.findById(instructorId).orElse(null);

        Course course = courseService.getCourseById(courseId);

        if(course == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Course not found");
        }

        if (!course.getInstructor().equals(instructor)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Instructor has no authority on this course");
        }
        Lesson lesson = new Lesson();
        if(lessonDTO.getTitle() != null)
            lesson.setTitle(lessonDTO.getTitle());
        if(lessonDTO.getContent() != null)
            lesson.setContent(lessonDTO.getContent());

        // Notify all enrolled students about the new lesson
        List<Student> enrolledStudents = course.getEnrolledStudents();
        String message = "A new lesson titled '" + lesson.getTitle() + "' has been added to the course: '" + course.getTitle() + "'.";
        for (Student student : enrolledStudents) {
            notificationService.createNotification(student.getId(), message);
        }

        return courseService.addLesson(courseId, lesson);
    }

    public QuestionBank addQuestionToBank(Long instructorId, Long courseId, QuestionDTO questionDTO){
        Instructor instructor = getInstructorById(instructorId);
        Course course = courseService.getCourseById(courseId);
        if (!course.getInstructor().equals(instructor)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Instructor has no authority on this course");
        }
        QuestionBank questionBank = course.getQuestionBank();
        Question question = questionService.createQuestion(questionBank, questionDTO);
        List<Question> questions = questionBank.getQuestions();
        questions.add(question);
        questionBank.setQuestions(questions);
        return questionBankService.saveQuestionBank(questionBank);
    }

    public Quiz createQuiz(Long instructorId, Long courseId, QuizDTO quizDTO) {
        if(Objects.equals(instructorId, courseService.getCourseById(courseId).getInstructor().getId())) {
            Course course = courseService.getCourseById(courseId);
            Quiz quiz = quizService.createQuiz(course, quizDTO);
            List<Quiz> quizzes = course.getQuizzes();
            quizzes.add(quiz);
            course.setQuizzes(quizzes);

            // Notify all enrolled students about the new quiz
            List<Student> enrolledStudents = course.getEnrolledStudents();
            String message = "A new quiz titled '" + quiz.getTitle() + "' has been added to the course: '" + course.getTitle() + "'.";
            for (Student student : enrolledStudents) {
                notificationService.createNotification(student.getId(), message);
            }
            return quiz;
        }
        throw new IllegalArgumentException("Course does not belong to the instructor");
    }

    public void removeStudentFromCourse(Long instructorId, Long courseId, Long studentId) {

        Student student = studentService.getStudentById(studentId);

        Instructor instructor = instructorRepository.findById(instructorId).orElse(null);

        Course course = courseService.getCourseById(courseId);

        if (!course.getInstructor().equals(instructor)) {
            throw new IllegalArgumentException("Course does not belong to the instructor");
        }
        courseService.removeStudentFromCourse(course, student);

        // Notify the student about removal from the course
        String message = "You have been removed from the course: '" + course.getTitle() + "'.";
        notificationService.createNotification(student.getId(),message);

    }

    // Method to save media file path for a course
    public MediaFile saveMediaFile(Long instructorId, Long courseId, String filePath) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        Course course = courseService.getCourseById(courseId);

        if (course == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Course not found");
        }

        if (!course.getInstructor().equals(instructor)) {
            throw new RuntimeException("Instructor is not associated with the course");
        }

        MediaFile mediaFile = new MediaFile(filePath, course);

        return mediaFileRepository.save(mediaFile);
    }

    public Instructor updateInstructorProfile(Long instructorId, InstructorDTO instructorDTO) {
        Instructor instructor = instructorRepository.findById(instructorId).orElse(null);

        if (instructor != null) {
            if (instructorDTO.getName() != null)
                instructor.setName(instructorDTO.getName());
            if (instructorDTO.getEmail() != null)
                instructor.setEmail(instructorDTO.getEmail());
            if (instructorDTO.getDepartment() != null)
                instructor.setDepartment(instructorDTO.getDepartment());
            if (instructorDTO.getEmployeeId() != null)
                instructor.setEmployeeId(instructorDTO.getEmployeeId());
            if (instructorDTO.getPassword() != null){
                instructor.setPassword(bCryptPasswordEncoder.encode(instructorDTO.getPassword()));
            }

            return instructorRepository.save(instructor);
        }
        throw new IllegalArgumentException("Student not found");
    }

    public Course addAssignmentToCourse(Long instructorId, Long courseId, AssignmentDTO assignmentDTO) {
        Instructor instructor = instructorRepository.findById(instructorId).orElse(null);

        Course course = courseService.getCourseById(courseId);

        if (course == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Course not found");
        }

        if (!course.getInstructor().equals(instructor)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Instructor has no authority on this course");
        }

        assignmentDTO.setCourseId(courseId);

        // Notify all enrolled students about the new assignment
        List<Student> enrolledStudents = course.getEnrolledStudents();
        String message = "A new assignment titled '" + assignmentDTO.getTitle() + "' has been added to the course: '" + course.getTitle() + "'.";
        for (Student student : enrolledStudents) {
            notificationService.createNotification(student.getId(), message); // Use NotificationService
        }


        return courseService.addAssignment(assignmentDTO);
    }

    public ResponseEntity<String> gradeAssignment(Long assignmentId, Long studentId, int grade) {
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        Student student = studentService.getStudentById(studentId);
        if (assignment != null && student != null) {
            if(student.getAssginmentsGrades().containsKey(assignmentId))
                return ResponseEntity.badRequest().body("This assignment has already been graded for this student.");
            if (!assignment.getSubmissions().containsKey(studentId))
                grade = 0;
            String studentGrade = grade + " / " + assignment.getMark();
            Map<Long, String> studentGrades = student.getAssginmentsGrades();
            studentGrades.put(assignment.getId(), studentGrade);
            student.setAssginmentsGrades(studentGrades);
            studentService.saveStudent(student);

            // Notify the student about their grade
            String message = "You have been graded for the assignment '" + assignment.getTitle() +
                    "'. Your grade is: " + studentGrade + ".";
            notificationService.createNotification(student.getId(), message);

            return ResponseEntity.ok("Grade submitted successfully.");
        }
        return ResponseEntity.badRequest().body("Failed to submit grade.");
    }

}
