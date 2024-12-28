package com.example.demo.serviceTest;

import com.example.demo.dtos.*;
import com.example.demo.models.*;
import com.example.demo.repository.InstructorRepository;
import com.example.demo.repository.MediaFileRepository;
import com.example.demo.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private CourseService courseService;
    @Mock
    private QuestionService questionService;
    @Mock
    private MediaFileRepository mediaFileRepository;
    @Mock
    private StudentService studentService;
    @Mock
    private QuizService quizService;
    @Mock
    private AssignmentService assignmentService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private QuestionBankService questionBankService;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private InstructorService instructorService;

    private Instructor testInstructor;
    private Course testCourse;
    private Student testStudent;
    private Quiz testQuiz;
    private Assignment testAssignment;
    private Question testQuestion;
    private QuestionBank testQuestionBank;

    @BeforeEach
    void setUp() {
        testInstructor = new Instructor();
        testInstructor.setId(1L);
        testInstructor.setName("Test Instructor");
        testInstructor.setEmail("test@example.com");
        testInstructor.setPassword("password");
        testInstructor.setDepartment("Test Department");
        testInstructor.setEmployeeId("EMP123");
        testInstructor.setCourses(new ArrayList<>());

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Test Course");
        testCourse.setInstructor(testInstructor);
        testCourse.setEnrolledStudents(new ArrayList<>());

        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setName("Test Student");

        testQuestionBank = new QuestionBank();
        testQuestionBank.setId(1L);
        testQuestionBank.setQuestions(new ArrayList<>());

        testCourse.setQuestionBank(testQuestionBank);
    }

    @Test
    void getAllInstructors_Success() {
        List<Instructor> instructors = Arrays.asList(testInstructor);
        when(instructorRepository.findAll()).thenReturn(instructors);

        List<Instructor> result = instructorService.getAllInstructors();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(instructorRepository).findAll();
    }

    @Test
    void getInstructorById_Success() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(testInstructor));

        Instructor result = instructorService.getInstructorById(1L);

        assertNotNull(result);
        assertEquals(testInstructor.getName(), result.getName());
        verify(instructorRepository).findById(1L);
    }

    @Test
    void createInstructor_Success() {
        when(instructorRepository.save(any(Instructor.class))).thenReturn(testInstructor);

        Instructor result = instructorService.createInstructor(testInstructor);

        assertNotNull(result);
        assertEquals(testInstructor.getName(), result.getName());
        verify(instructorRepository).save(testInstructor);
    }

    @Test
    void updateInstructor_Success() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(testInstructor));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(testInstructor);

        Instructor updatedInstructor = new Instructor();
        updatedInstructor.setName("Updated Name");

        Instructor result = instructorService.updateInstructor(1L, updatedInstructor);

        assertNotNull(result);
        verify(instructorRepository).save(any(Instructor.class));
    }

    @Test
    void createCourse_Success() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setTitle("Test Course");

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(testInstructor));
        when(courseService.createCourse(any(Instructor.class), any(CourseDTO.class))).thenReturn(testCourse);
        when(instructorRepository.save(any(Instructor.class))).thenReturn(testInstructor);

        Course result = instructorService.createCourse(1L, courseDTO);

        assertNotNull(result);
        verify(courseService).createCourse(any(Instructor.class), any(CourseDTO.class));
    }

    @Test
    void updateCourse_Success() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setTitle("Updated Course");

        when(courseService.getCourseById(1L)).thenReturn(testCourse);
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(testInstructor));
        when(courseService.updateCourse(anyLong(), any(CourseDTO.class))).thenReturn(testCourse);

        Course result = instructorService.updateCourse(1L, 1L, courseDTO);

        assertNotNull(result);
        verify(courseService).updateCourse(anyLong(), any(CourseDTO.class));
    }
    @Test
    void addQuestionToBank_Success() {
        // Create a list of options
        List<String> options = Arrays.asList("Option 1", "Option 2", "Option 3", "Option 4");

        // Create QuestionDTO with the correct constructor parameters
        QuestionDTO questionDTO = new QuestionDTO(
                "Test Question",  // question
                options,         // options
                "Option 1",      // correctAnswer
                1L              // QuestionBankId
        );

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(testInstructor));
        when(courseService.getCourseById(1L)).thenReturn(testCourse);
        when(questionService.createQuestion(any(QuestionBank.class), any(QuestionDTO.class))).thenReturn(testQuestion);
        when(questionBankService.saveQuestionBank(any(QuestionBank.class))).thenReturn(testQuestionBank);

        QuestionBank result = instructorService.addQuestionToBank(1L, 1L, questionDTO);

        assertNotNull(result);
        verify(questionBankService).saveQuestionBank(any(QuestionBank.class));
    }


    @Test
    void updateInstructorProfile_Success() {
        InstructorDTO instructorDTO = new InstructorDTO();
        instructorDTO.setName("Updated Name");
        instructorDTO.setPassword("newPassword");

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(testInstructor));
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(instructorRepository.save(any(Instructor.class))).thenReturn(testInstructor);

        Instructor result = instructorService.updateInstructorProfile(1L, instructorDTO);

        assertNotNull(result);
        verify(instructorRepository).save(any(Instructor.class));
    }
}