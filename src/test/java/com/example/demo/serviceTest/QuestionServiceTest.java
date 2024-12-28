package com.example.demo.serviceTest;

import com.example.demo.dtos.QuestionDTO;
import com.example.demo.models.Question;
import com.example.demo.models.QuestionBank;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.service.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    private Question testQuestion;
    private QuestionDTO testQuestionDTO;
    private QuestionBank testQuestionBank;
    private List<String> testOptions;

    @BeforeEach
    void setUp() {
        testOptions = Arrays.asList("Option 1", "Option 2", "Option 3", "Option 4");

        testQuestionBank = new QuestionBank();
        testQuestionBank.setId(1L);

        testQuestion = new Question();
        testQuestion.setId(1L);
        testQuestion.setQuestion("Test Question");
        testQuestion.setOptions(testOptions);
        testQuestion.setCorrectAnswer("Option 1");
        testQuestion.setQuestionBank(testQuestionBank);
        testQuestionDTO = new QuestionDTO(
                "Test Question", testOptions,
                "Option 1",
                1L
        );
    }

    @Test
    void getQuestionById_Success() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(testQuestion));

        Question result = questionService.getQuestionById(1L);

        assertNotNull(result);
        assertEquals(testQuestion.getQuestion(), result.getQuestion());
        assertEquals(testQuestion.getCorrectAnswer(), result.getCorrectAnswer());
        verify(questionRepository).findById(1L);
    }

    @Test
    void getQuestionById_NotFound() {
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        Question result = questionService.getQuestionById(1L);

        assertNull(result);
        verify(questionRepository).findById(1L);
    }

    @Test
    void getAllQuestions_Success() {
        List<Question> questions = Arrays.asList(testQuestion);
        when(questionRepository.findAll()).thenReturn(questions);

        List<Question> result = questionService.getAllQuestions();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testQuestion.getQuestion(), result.get(0).getQuestion());
        verify(questionRepository).findAll();
    }

    @Test
    void createQuestion_FromEntity_Success() {
        when(questionRepository.save(any(Question.class))).thenReturn(testQuestion);

        Question result = questionService.createQuestion(testQuestion);

        assertNotNull(result);
        assertEquals(testQuestion.getQuestion(), result.getQuestion());
        assertEquals(testQuestion.getCorrectAnswer(), result.getCorrectAnswer());
        verify(questionRepository).save(testQuestion);
    }

    @Test
    void createQuestion_FromDTO_Success() {
        when(questionRepository.save(any(Question.class))).thenReturn(testQuestion);

        Question result = questionService.createQuestion(testQuestionBank, testQuestionDTO);

        assertNotNull(result);
        assertEquals(testQuestionDTO.getQuestion(), result.getQuestion());
        assertEquals(testQuestionDTO.getCorrectAnswer(), result.getCorrectAnswer());
        assertEquals(testQuestionDTO.getOptions(), result.getOptions());
        verify(questionRepository).save(any(Question.class));
    }

    @Test
    void updateQuestion_Success() {
        Question updatedQuestion = new Question();
        updatedQuestion.setQuestion("Updated Question");
        updatedQuestion.setOptions(testOptions);
        updatedQuestion.setCorrectAnswer("Option 2");
        updatedQuestion.setQuestionBank(testQuestionBank);

        when(questionRepository.findById(1L)).thenReturn(Optional.of(testQuestion));
        when(questionRepository.save(any(Question.class))).thenReturn(updatedQuestion);

        Question result = questionService.updateQuestion(1L, updatedQuestion);

        assertNotNull(result);
        assertEquals(updatedQuestion.getQuestion(), result.getQuestion());
        assertEquals(updatedQuestion.getCorrectAnswer(), result.getCorrectAnswer());
        verify(questionRepository).save(any(Question.class));
    }

    @Test
    void updateQuestion_NotFound() {
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        Question result = questionService.updateQuestion(1L, testQuestion);

        assertNull(result);
        verify(questionRepository).findById(1L);
        verify(questionRepository, never()).save(any(Question.class));
    }

    @Test
    void deleteQuestion_Success() {
        doNothing().when(questionRepository).deleteById(1L);

        questionService.deleteQuestion(1L);

        verify(questionRepository).deleteById(1L);
    }

}