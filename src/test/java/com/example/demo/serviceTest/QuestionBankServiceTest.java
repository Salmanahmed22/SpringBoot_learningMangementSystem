package com.example.demo.serviceTest;

import com.example.demo.models.Question;
import com.example.demo.models.QuestionBank;
import com.example.demo.repository.QuestionBankRepository;
import com.example.demo.service.QuestionBankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class QuestionBankServiceTest {

    @Mock
    private QuestionBankRepository questionBankRepository;

    @InjectMocks
    private QuestionBankService questionBankService;

    private QuestionBank testQuestionBank;
    private Question testQuestion;

    @BeforeEach
    void setUp() {
        testQuestion = new Question();
        testQuestion.setId(1L);
        testQuestion.setQuestion("Test Question");
        testQuestion.setCorrectAnswer("Test Answer");
        testQuestion.setOptions(Arrays.asList("Option 1", "Option 2", "Option 3", "Option 4"));

        testQuestionBank = new QuestionBank();
        testQuestionBank.setId(1L);
        testQuestionBank.setQuestions(new ArrayList<>());
    }

    @Test
    void getQuestionBankById_Success() {
        when(questionBankRepository.findById(1L)).thenReturn(Optional.of(testQuestionBank));

        QuestionBank result = questionBankService.getQuestionBankById(1L);

        assertNotNull(result);
        assertEquals(testQuestionBank.getId(), result.getId());
        verify(questionBankRepository).findById(1L);
    }

    @Test
    void getQuestionBankById_NotFound() {
        when(questionBankRepository.findById(1L)).thenReturn(Optional.empty());

        QuestionBank result = questionBankService.getQuestionBankById(1L);

        assertNull(result);
        verify(questionBankRepository).findById(1L);
    }

    @Test
    void saveQuestionBank_Success() {
        when(questionBankRepository.save(any(QuestionBank.class))).thenReturn(testQuestionBank);

        QuestionBank result = questionBankService.saveQuestionBank(testQuestionBank);

        assertNotNull(result);
        assertEquals(testQuestionBank.getId(), result.getId());
        verify(questionBankRepository).save(testQuestionBank);
    }

    @Test
    void addQuestion_Success() {
        testQuestionBank.setQuestions(new ArrayList<>());
        when(questionBankRepository.findById(1L)).thenReturn(Optional.of(testQuestionBank));

        questionBankService.addQuestion(1L, testQuestion);

        assertEquals(1, testQuestionBank.getQuestions().size());
        assertEquals(testQuestion, testQuestionBank.getQuestions().get(0));
        verify(questionBankRepository).findById(1L);
    }

    @Test
    void addQuestion_QuestionBankNotFound() {
        when(questionBankRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            questionBankService.addQuestion(1L, testQuestion);
        });

        verify(questionBankRepository).findById(1L);
    }

    @Test
    void removeQuestion_Success() {
        doNothing().when(questionBankRepository).deleteById(1L);

        questionBankService.removeQuestion(testQuestion);

        verify(questionBankRepository).deleteById(1L);
    }

    @Test
    void addQuestion_WithExistingQuestions() {
        List<Question> existingQuestions = new ArrayList<>();
        Question existingQuestion = new Question();
        existingQuestion.setId(2L);
        existingQuestions.add(existingQuestion);

        testQuestionBank.setQuestions(existingQuestions);

        when(questionBankRepository.findById(1L)).thenReturn(Optional.of(testQuestionBank));

        questionBankService.addQuestion(1L, testQuestion);

        assertEquals(2, testQuestionBank.getQuestions().size());
        assertTrue(testQuestionBank.getQuestions().contains(testQuestion));
        verify(questionBankRepository).findById(1L);
    }

    @Test
    void addQuestion_WithNullQuestionsList() {
        testQuestionBank.setQuestions(null);
        when(questionBankRepository.findById(1L)).thenReturn(Optional.of(testQuestionBank));

        assertThrows(NullPointerException.class, () -> {
            questionBankService.addQuestion(1L, testQuestion);
        });

        verify(questionBankRepository).findById(1L);
    }

    @Test
    void removeQuestion_WithNullQuestion() {
        assertThrows(NullPointerException.class, () -> {
            questionBankService.removeQuestion(null);
        });

        verify(questionBankRepository, never()).deleteById(any());
    }

    @Test
    void saveQuestionBank_WithNullQuestionBank() {
        when(questionBankRepository.save(null)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            questionBankService.saveQuestionBank(null);
        });

        verify(questionBankRepository).save(null);
    }
}