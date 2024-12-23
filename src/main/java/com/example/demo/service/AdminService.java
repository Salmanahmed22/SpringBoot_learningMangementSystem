package com.example.demo.service;

import com.example.demo.dtos.*;
import com.example.demo.models.*;
import com.example.demo.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private InstructorService instructorService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private LessonService lessonService;
    //    @Autowired
//    private MediaFileService mediaFileService;
//    @Autowired
//    private NotificationService notificationService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuizService quizService;
    @Autowired
    private UserService userService;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private QuestionBankService questionBankService;

    // Methods to Access Admin Repo
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }


    // User Creation Update and Deletion methods

    // User Creation
    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public Instructor createInstructor(Instructor instructor) {
        return instructorService.createInstructor(instructor);
    }

    public Student createStudent(Student student) {
        return studentService.createStudent(student);
    }


    // User Update
    public Admin updateAdmin(Long id, Admin updatedAdmin) {
        if (adminRepository.existsByEmailAndIdNot(updatedAdmin.getEmail(), id)) {
            throw new IllegalArgumentException("Email already exists for another admin.");
        }

        Admin existingAdmin = adminRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Admin with ID " + id + " not found"));

        if (existingAdmin != null) {
            if (updatedAdmin.getName() != null) {
                existingAdmin.setName(updatedAdmin.getName());
            }

            if (updatedAdmin.getPassword() != null) {
                existingAdmin.setPassword(bCryptPasswordEncoder.encode(updatedAdmin.getPassword()));
            }
            if (updatedAdmin.getEmail() != null) {
                existingAdmin.setEmail(updatedAdmin.getEmail());
            }

            return adminRepository.save(existingAdmin);
        }
        return null;
    }

    public Instructor updateInstructor(Long id, Instructor updatedInstructor) {
        return instructorService.updateInstructor(id, updatedInstructor);
    }

    public Student updateStudent(Long id, StudentDTO updatedStudentDTO) {
        return studentService.updateStudentProfile(id, updatedStudentDTO);
    }

    // User Deletion

    public void deleteUser(Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            if (user.getRole().equals(Role.STUDENT))
                studentService.deleteStudent(id);
            else if (user.getRole().equals(Role.INSTRUCTOR))
                instructorService.deleteInstructor(id);
            else
                adminRepository.deleteById(id);
        }
    }


    // Creation

    //done
    public Course createCourse(CourseDTO courseDTO) {
        Instructor instructor = instructorService.getInstructorById(courseDTO.getInstructorId());
        return courseService.createCourse(instructor, courseDTO);
    }

    //done
    public Lesson createLesson(LessonDTO lessonDTO) {
        return lessonService.createLesson(lessonDTO);
    }

    //done
    public Assignment createAssignment(AssignmentDTO assignmentDTO) {
        return assignmentService.createAssignment(assignmentDTO);
    }

    //done
    public Quiz createQuiz(QuizDTO quizDTO) {
        Course course = courseService.getCourseById(quizDTO.getCourseId());
        return quizService.createQuiz(course, quizDTO);
    }

    //done
    public Question createQuestion(Long questionBankId, QuestionDTO questionDTO) {
        QuestionBank questionBank = questionBankService.getQuestionBankById(questionBankId);
        return questionService.createQuestion(questionBank, questionDTO);
    }

//    public MediaFile createMediaFile(MediaFile mediaFile) {
//        return mediaFileService.createMediaFile(mediaFile);
//    }
//
//    public Notification createNotification(Notification notification) {
//        return notificationService.createNotification(notification);
//    }

    // Update

    public Course updateCourse(Long id, CourseDTO updatedCourseDTO) {
        Instructor instructor = instructorService.getInstructorById(updatedCourseDTO.getInstructorId());
        return courseService.updateCourse(id, updatedCourseDTO);
    }

    //done
    public Assignment updateAssignment(Long id, AssignmentDTO updatedAssignmentDTO) {
        Assignment updatedAssignment = assignmentService.createAssignment(updatedAssignmentDTO);
        return assignmentService.updateAssignment(id, updatedAssignment);
    }

    //done
    public Quiz updateQuiz(Long id, QuizDTO updatedQuizDTO) {
        Course course = courseService.getCourseById(updatedQuizDTO.getCourseId());
        Quiz updatedQuiz = quizService.createQuiz(course, updatedQuizDTO);
        return quizService.updateQuiz(id, updatedQuiz);
    }

    //done
    public Lesson updateLesson(Long id, LessonDTO updatedLessonDTO) {
        Lesson updatedLesson = lessonService.createLesson(updatedLessonDTO);
        return lessonService.updateLesson(id, updatedLesson);
    }

//    public MediaFile updateMediaFile(Long id, MediaFile updatedMediaFile) {
//        return mediaFileService.updateMediaFile(id, updatedMediaFile);
//    }
//
//    public Notification updateNotification(Long id, Notification updatedNotification) {
//        return notificationService.updateNotification(id, updatedNotification);
//    }

    // Update Question
    public Question updateQuestion(Long id, QuestionDTO updatedQuestionDTO) {
        Question updatedQuestion = questionService.createQuestion(questionBankService.getQuestionBankById(updatedQuestionDTO.getQuestionBankId()),
                updatedQuestionDTO);
        return questionService.updateQuestion(id, updatedQuestion);
    }




    // Deletion

    //done
    public void deleteAssignment(Long id) {
        assignmentService.deleteAssignment(id);
    }

    //done
    public void deleteCourse(Long id) {
        courseService.deleteCourse(id);
    }

    //done
    public void deleteLesson(Long id) {
        lessonService.deleteLesson(id);
    }

    //    public void deleteMediaFile(Long id) {
//        mediaFileService.deleteMediaFile(id);
//    }
//
//    public void deleteNotification(Long id) {
//        notificationService.deleteNotification(id);
//    }

    //done
    public void deleteQuestion(Long id) {
        questionService.deleteQuestion(id);
    }

    //done
    public void deleteQuiz(Long id) {
        quizService.deleteQuiz(id);
    }

}
