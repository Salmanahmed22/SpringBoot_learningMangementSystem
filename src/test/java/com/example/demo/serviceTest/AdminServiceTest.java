package com.example.demo.serviceTest;

import com.example.demo.dtos.*;
import com.example.demo.models.*;
import com.example.demo.repository.AdminRepository;
import com.example.demo.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;
    @Mock
    private AssignmentService assignmentService;
    @Mock
    private CourseService courseService;
    @Mock
    private InstructorService instructorService;
    @Mock
    private StudentService studentService;
    @Mock
    private LessonService lessonService;
    @Mock
    private QuestionService questionService;
    @Mock
    private QuizService quizService;
    @Mock
    private UserService userService;
    @Mock
    private SubmissionService submissionService;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private QuestionBankService questionBankService;

    @InjectMocks
    private AdminService adminService;

    private Admin testAdmin;
    private Instructor testInstructor;
    private Student testStudent;
    private Course testCourse;
    private Lesson testLesson;
    private Quiz testQuiz;
    private Question testQuestion;
    private Assignment testAssignment;

    @BeforeEach
    void setUp() {
        testAdmin = new Admin();
        testAdmin.setId(1L);
        testAdmin.setName("Test Admin");
        testAdmin.setEmail("admin@test.com");
        testAdmin.setPassword("password");

        testInstructor = new Instructor();
        testInstructor.setId(1L);

        testStudent = new Student();
        testStudent.setId(1L);

        testCourse = new Course();
        testCourse.setId(1L);

        testLesson = new Lesson();
        testLesson.setId(1L);

        testQuiz = new Quiz();
        testQuiz.setId(1L);

        testQuestion = new Question();
        testQuestion.setId(1L);

        testAssignment = new Assignment();
        testAssignment.setId(1L);
    }

    @Test
    void getAllAdmins_Success() {
        List<Admin> admins = Arrays.asList(testAdmin);
        when(adminRepository.findAll()).thenReturn(admins);

        List<Admin> result = adminService.getAllAdmins();

        assertEquals(admins, result);
        verify(adminRepository).findAll();
    }

    @Test
    void getAdminById_Success() {
        when(adminRepository.findById(1L)).thenReturn(Optional.of(testAdmin));

        Admin result = adminService.getAdminById(1L);

        assertEquals(testAdmin, result);
        verify(adminRepository).findById(1L);
    }

    @Test
    void createAdmin_Success() {
        when(adminRepository.save(any(Admin.class))).thenReturn(testAdmin);

        Admin result = adminService.createAdmin(testAdmin);

        assertEquals(testAdmin, result);
        verify(adminRepository).save(testAdmin);
    }

    @Test
    void updateAdmin_Success() {
        Admin updatedAdmin = new Admin();
        updatedAdmin.setName("Updated Name");
        updatedAdmin.setEmail("updated@test.com");
        updatedAdmin.setPassword("newpassword");

        when(adminRepository.findById(1L)).thenReturn(Optional.of(testAdmin));
        when(adminRepository.existsByEmailAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(adminRepository.save(any(Admin.class))).thenReturn(updatedAdmin);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");

        Admin result = adminService.updateAdmin(1L, updatedAdmin);

        assertNotNull(result);
        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void updateAdmin_EmailExists() {
        Admin updatedAdmin = new Admin();
        updatedAdmin.setEmail("existing@test.com");

        when(adminRepository.existsByEmailAndIdNot(anyString(), anyLong())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                adminService.updateAdmin(1L, updatedAdmin)
        );
    }

    @Test
    void deleteUser_AdminRole() {
        User adminUser = new User();
        adminUser.setRole(Role.ADMIN);
        when(userService.getUserById(1L)).thenReturn(adminUser);

        adminService.deleteUser(1L);

        verify(adminRepository).deleteById(1L);
    }

    @Test
    void createCourse_Success() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setInstructorId(1L);

        when(instructorService.getInstructorById(1L)).thenReturn(testInstructor);
        when(courseService.createCourse(any(Instructor.class), any(CourseDTO.class))).thenReturn(testCourse);

        Course result = adminService.createCourse(courseDTO);

        assertEquals(testCourse, result);
        verify(courseService).createCourse(any(Instructor.class), any(CourseDTO.class));
    }

    @Test
    void createLesson_Success() {
        LessonDTO lessonDTO = new LessonDTO();
        when(lessonService.createLesson(any(LessonDTO.class))).thenReturn(testLesson);

        Lesson result = adminService.createLesson(lessonDTO);

        assertEquals(testLesson, result);
        verify(lessonService).createLesson(lessonDTO);
    }

    @Test
    void createQuiz_Success() {
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setCourseId(1L);

        when(courseService.getCourseById(1L)).thenReturn(testCourse);
        when(quizService.createQuiz(any(Course.class), any(QuizDTO.class))).thenReturn(testQuiz);

        Quiz result = adminService.createQuiz(quizDTO);

        assertEquals(testQuiz, result);
        verify(quizService).createQuiz(any(Course.class), any(QuizDTO.class));
    }

    @Test
    void deleteAssignment_Success() {
        doNothing().when(assignmentService).deleteAssignment(1L);

        adminService.deleteAssignment(1L);

        verify(assignmentService).deleteAssignment(1L);
    }

    @Test
    void deleteCourse_Success() {
        doNothing().when(courseService).deleteCourse(1L);

        adminService.deleteCourse(1L);

        verify(courseService).deleteCourse(1L);
    }
}