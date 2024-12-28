package com.example.demo.serviceTest;

import com.example.demo.dtos.QuizDTO;
import com.example.demo.models.*;
import com.example.demo.repository.QuizRepository;
import com.example.demo.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;
    @Mock
    private StudentService studentService;
    @Mock
    private QuestionService questionService;
    @Mock
    private SubmissionService submissionService;
    @Mock
    private CourseService courseService;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private QuizService quizService;

    private Quiz testQuiz;
    private QuizDTO testQuizDTO;
    private Course testCourse;
    private Student testStudent;
    private Question testQuestion1;
    private Question testQuestion2;
    private QuestionBank testQuestionBank;
    private Submission testSubmission;

    @BeforeEach
    void setUp() {
        // Setup Course
        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Test Course");

        // Setup Questions
        testQuestion1 = new Question();
        testQuestion1.setId(1L);
        testQuestion1.setQuestion("Question 1");
        testQuestion1.setCorrectAnswer("Answer 1");

        testQuestion2 = new Question();
        testQuestion2.setId(2L);
        testQuestion2.setQuestion("Question 2");
        testQuestion2.setCorrectAnswer("Answer 2");

        // Setup QuestionBank
        testQuestionBank = new QuestionBank();
        testQuestionBank.setId(1L);
        testQuestionBank.setQuestions(Arrays.asList(testQuestion1, testQuestion2));
        testCourse.setQuestionBank(testQuestionBank);

        // Setup Quiz
        testQuiz = new Quiz();
        testQuiz.setId(1L);
        testQuiz.setTitle("Test Quiz");
        testQuiz.setDescription("Test Description");
        testQuiz.setCourse(testCourse);
        testQuiz.setQuizDate(LocalDateTime.now());
        testQuiz.setDuration(Duration.ofDays(60));
        testQuiz.setNumOfQuestions(2);
        testQuiz.setQuestions(Arrays.asList(testQuestion1, testQuestion2));
        testQuiz.setSubmissions(new ArrayList<>());

        // Setup QuizDTO
        testQuizDTO = new QuizDTO();
        testQuizDTO.setTitle("Test Quiz");
        testQuizDTO.setDescription("Test Description");
        testQuizDTO.setQuizDate(LocalDateTime.now());
        testQuizDTO.setDuration(Duration.ofDays(60));
        testQuizDTO.setNumOfQuestions(2);
        testQuizDTO.setCourseId(1L);

        // Setup Student
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setQuizGrades(new HashMap<>());

        // Setup Submission
        testSubmission = new Submission();
        testSubmission.setId(1L);
        testSubmission.setAnswers(Arrays.asList("Answer 1", "Answer 2"));
    }

    @Test
    void getQuizById_Success() {
        when(quizRepository.findById(1L)).thenReturn(Optional.of(testQuiz));

        Quiz result = quizService.getQuizById(1L);

        assertNotNull(result);
        assertEquals(testQuiz.getTitle(), result.getTitle());
        verify(quizRepository).findById(1L);
    }

    @Test
    void getAllQuizzes_Success() {
        List<Quiz> quizzes = Arrays.asList(testQuiz);
        when(quizRepository.findAll()).thenReturn(quizzes);

        List<Quiz> result = quizService.getAllQuizzes();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(quizRepository).findAll();
    }

    @Test
    void createQuiz_Success() {
        when(quizRepository.save(any(Quiz.class))).thenReturn(testQuiz);

        Quiz result = quizService.createQuiz(testCourse, testQuizDTO);

        assertNotNull(result);
        assertEquals(testQuizDTO.getTitle(), result.getTitle());
        assertEquals(testQuizDTO.getNumOfQuestions(), result.getQuestions().size());
        verify(quizRepository).save(any(Quiz.class));
    }

    @Test
    void createQuiz_NotEnoughQuestions() {
        testQuizDTO.setNumOfQuestions(5); // More than available questions

        assertThrows(IllegalStateException.class, () -> {
            quizService.createQuiz(testCourse, testQuizDTO);
        });
    }


    @Test
    void calculateGrades_Success() {
        String result = quizService.calculateGrades(testQuiz, testSubmission);

        assertEquals("2 / 2", result);
    }

    @Test
    void updateQuiz_Success() {
        Quiz updatedQuiz = new Quiz();
        updatedQuiz.setTitle("Updated Quiz");
        updatedQuiz.setDescription("Updated Description");
        updatedQuiz.setCourse(testCourse);

        when(quizRepository.findById(1L)).thenReturn(Optional.of(testQuiz));
        when(quizRepository.save(any(Quiz.class))).thenReturn(updatedQuiz);

        Quiz result = quizService.updateQuiz(1L, updatedQuiz);

        assertNotNull(result);
        assertEquals(updatedQuiz.getTitle(), result.getTitle());
        verify(quizRepository).save(any(Quiz.class));
    }



    @Test
    void calculateGrades_PartialCorrect() {
        testSubmission.setAnswers(Arrays.asList("Answer 1", "Wrong Answer"));

        String result = quizService.calculateGrades(testQuiz, testSubmission);

        assertEquals("1 / 2", result);
    }

}
