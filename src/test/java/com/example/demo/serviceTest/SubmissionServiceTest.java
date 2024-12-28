package com.example.demo.serviceTest;

import com.example.demo.models.*;
import com.example.demo.repository.SubmissionRepository;
import com.example.demo.service.QuestionService;
import com.example.demo.service.SubmissionService;
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
public class SubmissionServiceTest {

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private SubmissionService submissionService;

    private Submission testSubmission;
    private Student testStudent;
    private Quiz testQuiz;

    @BeforeEach
    void setUp() {
        // Setup Student
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setName("Test Student");

        // Setup Quiz
        testQuiz = new Quiz();
        testQuiz.setId(1L);
        testQuiz.setTitle("Test Quiz");

        // Setup Submission
        testSubmission = new Submission();
        testSubmission.setId(1L);
        testSubmission.setStudent(testStudent);
        testSubmission.setQuiz(testQuiz);
        testSubmission.setAnswers(Arrays.asList("Answer 1", "Answer 2"));
        testSubmission.setGrade("2/2");
    }

    @Test
    void getSubmissionById_Success() {
        when(submissionRepository.findById(1L)).thenReturn(Optional.of(testSubmission));

        Submission result = submissionService.getSubmissionById(1L);

        assertNotNull(result);
        assertEquals(testSubmission.getId(), result.getId());
        assertEquals(testSubmission.getAnswers(), result.getAnswers());
        assertEquals(testSubmission.getGrade(), result.getGrade());
        verify(submissionRepository).findById(1L);
    }

    @Test
    void getSubmissionById_NotFound() {
        when(submissionRepository.findById(1L)).thenReturn(Optional.empty());

        Submission result = submissionService.getSubmissionById(1L);

        assertNull(result);
        verify(submissionRepository).findById(1L);
    }

    @Test
    void getAllSubmissions_Success() {
        List<Submission> submissions = Arrays.asList(testSubmission);
        when(submissionRepository.findAll()).thenReturn(submissions);

        List<Submission> result = submissionService.getAllSubmissions();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testSubmission.getId(), result.get(0).getId());
        verify(submissionRepository).findAll();
    }

    @Test
    void createSubmission_Success() {
        when(submissionRepository.save(any(Submission.class))).thenReturn(testSubmission);

        Submission result = submissionService.createSubmission(testSubmission);

        assertNotNull(result);
        assertEquals(testSubmission.getId(), result.getId());
        assertEquals(testSubmission.getAnswers(), result.getAnswers());
        assertEquals(testSubmission.getGrade(), result.getGrade());
        verify(submissionRepository).save(testSubmission);
    }

    @Test
    void updateSubmission_Success() {
        Submission updatedSubmission = new Submission();
        updatedSubmission.setAnswers(Arrays.asList("New Answer 1", "New Answer 2"));
        updatedSubmission.setGrade("1/2");
        updatedSubmission.setStudent(testStudent);
        updatedSubmission.setQuiz(testQuiz);

        when(submissionRepository.findById(1L)).thenReturn(Optional.of(testSubmission));
        when(submissionRepository.save(any(Submission.class))).thenReturn(updatedSubmission);

        Submission result = submissionService.updateSubmission(1L, updatedSubmission);

        assertNotNull(result);
        assertEquals(updatedSubmission.getAnswers(), result.getAnswers());
        assertEquals(updatedSubmission.getGrade(), result.getGrade());
        verify(submissionRepository).save(any(Submission.class));
    }

    @Test
    void updateSubmission_NotFound() {
        when(submissionRepository.findById(1L)).thenReturn(Optional.empty());

        Submission result = submissionService.updateSubmission(1L, testSubmission);

        assertNull(result);
        verify(submissionRepository).findById(1L);
        verify(submissionRepository, never()).save(any(Submission.class));
    }

    @Test
    void deleteSubmission_Success() {
        doNothing().when(submissionRepository).deleteById(1L);

        submissionService.deleteSubmission(1L);

        verify(submissionRepository).deleteById(1L);
    }

    @Test
    void updateSubmission_PartialUpdate() {
        Submission partialUpdate = new Submission();
        partialUpdate.setAnswers(Arrays.asList("New Answer 1", "New Answer 2"));
        // Only updating answers, leaving other fields unchanged

        when(submissionRepository.findById(1L)).thenReturn(Optional.of(testSubmission));
        when(submissionRepository.save(any(Submission.class))).thenReturn(testSubmission);

        Submission result = submissionService.updateSubmission(1L, partialUpdate);

        assertNotNull(result);
        assertEquals(partialUpdate.getAnswers(), result.getAnswers());
        // Original fields should remain unchanged
        assertEquals(testSubmission.getStudent(), result.getStudent());
        assertEquals(testSubmission.getQuiz(), result.getQuiz());
        verify(submissionRepository).save(any(Submission.class));
    }

    @Test
    void getAllSubmissions_EmptyList() {
        when(submissionRepository.findAll()).thenReturn(Arrays.asList());

        List<Submission> result = submissionService.getAllSubmissions();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(submissionRepository).findAll();
    }
}
