package com.example.demo.service;

import com.example.demo.dtos.*;
import com.example.demo.models.*;
//import com.example.demo.models.Notification;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.MediaFileRepository;
import com.example.demo.repository.InstructorRepository;
//import com.example.demo.repository.NotificationRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.File;
import java.io.IOException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        courseRepository.save(course);
        return course;
    }

    public Course updateCourse(Long instructorId, Long courseId, CourseDTO updatedCourse) {
        Course course = courseService.getCourseById(courseId);
        Instructor instructor = getInstructorById(instructorId);
        if (!course.getInstructor().equals(instructor)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Instructor has no authority on this course");
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

        Course course = courseRepository.findById(courseId).orElseThrow(() ->
                new EntityNotFoundException("Course not found")
        );

        if (!course.getInstructor().equals(instructor)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Instructor has no authority on this course");
        }
        Lesson lesson = new Lesson();
        if(lessonDTO.getTitle() != null)
            lesson.setTitle(lessonDTO.getTitle());
        if(lessonDTO.getContent() != null)
            lesson.setContent(lessonDTO.getContent());

        return courseService.addLesson(course, lesson);
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

    }

    // Method to save media file path for a course
    public MediaFile saveMediaFile(Long instructorId, Long courseId, String filePath) {
        // Retrieve the instructor by ID
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        // Retrieve the course by ID
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Ensure the instructor is associated with the course
        if (!course.getInstructor().equals(instructor)) {
            throw new RuntimeException("Instructor is not associated with the course");
        }

        // Create a new MediaFile with the provided file path and course
        MediaFile mediaFile = new MediaFile(filePath, course);

        // Save the media file record in the database
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

        Course course = courseRepository.findById(courseId).orElseThrow(() ->
                new EntityNotFoundException("Course not found")
        );

        if (!course.getInstructor().equals(instructor)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Instructor has no authority on this course");
        }
        Assignment assignment = new Assignment();
            assignment.setTitle(assignmentDTO.getTitle());
            assignment.setDescription(assignmentDTO.getDescription());
            assignment.setDueDate(assignmentDTO.getDueDate());
            assignment.setMark(assignmentDTO.getMark());

        return courseService.addAssignment(course, assignment);
    }

    public ResponseEntity<String> gradeAssignment(Long assignmentId, Long studentId, int grade) {
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        Student student = studentService.getStudentById(studentId);
        if (assignment != null && student != null) {
            if(student.getGrades().containsKey(assignmentId))
                return ResponseEntity.badRequest().body("This assignment has already been graded for this student.");
            if (!assignment.getSubmissions().containsKey(studentId))
                grade = 0;
            String studentGrade = grade + " / " + assignment.getMark();
            Map<Long, String> studentGrades = student.getGrades();
            studentGrades.put(assignment.getId(), studentGrade);
            student.setGrades(studentGrades);
            studentService.saveStudent(student);

            return ResponseEntity.ok("Grade submitted successfully.");
        }
        return ResponseEntity.badRequest().body("Failed to submit grade.");
    }

}
