package com.example.demo.serviceTest;

import com.example.demo.dtos.AssignmentDTO;
import com.example.demo.models.Assignment;
import com.example.demo.models.Course;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.service.AssignmentService;
import com.example.demo.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private AssignmentService assignmentService;

    private Assignment testAssignment;
    private AssignmentDTO testAssignmentDTO;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Test Course");

        testAssignment = new Assignment();
        testAssignment.setId(1L);
        testAssignment.setTitle("Test Assignment");
        testAssignment.setDescription("Test Description");
        testAssignment.setMark(100);
        testAssignment.setDueDate(LocalDateTime.now().plusDays(7));
        testAssignment.setCourse(testCourse);
        testAssignment.setSubmissions(new HashMap<>());

        testAssignmentDTO = new AssignmentDTO();
        testAssignmentDTO.setTitle("Test Assignment");
        testAssignmentDTO.setDescription("Test Description");
        testAssignmentDTO.setMark(100);
        testAssignmentDTO.setDueDate(LocalDateTime.now().plusDays(7));
        testAssignmentDTO.setCourseId(1L);
    }

    @Test
    void getAssignmentById_Success() {
        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(testAssignment));

        Assignment result = assignmentService.getAssignmentById(1L);

        assertNotNull(result);
        assertEquals(testAssignment.getTitle(), result.getTitle());
        verify(assignmentRepository).findById(1L);
    }

    @Test
    void getAssignmentById_NotFound() {
        when(assignmentRepository.findById(1L)).thenReturn(Optional.empty());

        Assignment result = assignmentService.getAssignmentById(1L);

        assertNull(result);
        verify(assignmentRepository).findById(1L);
    }

    @Test
    void getAllAssignments_Success() {
        List<Assignment> assignments = Arrays.asList(testAssignment);
        when(assignmentRepository.findAll()).thenReturn(assignments);

        List<Assignment> result = assignmentService.getAllAssignments();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testAssignment.getTitle(), result.get(0).getTitle());
        verify(assignmentRepository).findAll();
    }

    @Test
    void createAssignment_FromDTO_Success() {
        when(courseService.getCourseById(1L)).thenReturn(testCourse);
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(testAssignment);

        Assignment result = assignmentService.createAssignment(testAssignmentDTO);

        assertNotNull(result);
        assertEquals(testAssignment.getTitle(), result.getTitle());
        assertEquals(testAssignment.getDescription(), result.getDescription());
        assertEquals(testAssignment.getMark(), result.getMark());
        verify(assignmentRepository).save(any(Assignment.class));
        verify(courseService).getCourseById(1L);
    }

    @Test
    void createAssignment_FromAssignment_Success() {
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(testAssignment);

        Assignment result = assignmentService.createAssignment(testAssignment);

        assertNotNull(result);
        assertEquals(testAssignment.getTitle(), result.getTitle());
        verify(assignmentRepository).save(testAssignment);
    }

    @Test
    void updateAssignment_Success() {
        Assignment updatedAssignment = new Assignment();
        updatedAssignment.setTitle("Updated Title");
        updatedAssignment.setDescription("Updated Description");
        updatedAssignment.setDueDate(LocalDateTime.now().plusDays(14));
        updatedAssignment.setCourse(testCourse);

        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(testAssignment));
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(updatedAssignment);

        Assignment result = assignmentService.updateAssignment(1L, updatedAssignment);

        assertNotNull(result);
        assertEquals(updatedAssignment.getTitle(), result.getTitle());
        assertEquals(updatedAssignment.getDescription(), result.getDescription());
        verify(assignmentRepository).save(any(Assignment.class));
    }

    @Test
    void updateAssignment_NotFound() {
        when(assignmentRepository.findById(1L)).thenReturn(Optional.empty());

        Assignment result = assignmentService.updateAssignment(1L, testAssignment);

        assertNull(result);
        verify(assignmentRepository).findById(1L);
        verify(assignmentRepository, never()).save(any(Assignment.class));
    }

    @Test
    void submitAssignment_Success() {
        Long studentId = 1L;
        String submissionContent = "Test Submission";
        testAssignment.setSubmissions(new HashMap<>());

        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(testAssignment));
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(testAssignment);

        Assignment result = assignmentService.submitAssignment(1L, studentId, submissionContent);

        assertNotNull(result);
        assertEquals(submissionContent, result.getSubmissions().get(studentId));
        verify(assignmentRepository).save(any(Assignment.class));
    }

    @Test
    void submitAssignment_NotFound() {
        when(assignmentRepository.findById(1L)).thenReturn(Optional.empty());

        Assignment result = assignmentService.submitAssignment(1L, 1L, "Test Submission");

        assertNull(result);
        verify(assignmentRepository).findById(1L);
        verify(assignmentRepository, never()).save(any(Assignment.class));
    }

    @Test
    void deleteAssignment_Success() {
        doNothing().when(assignmentRepository).deleteById(1L);

        assignmentService.deleteAssignment(1L);

        verify(assignmentRepository).deleteById(1L);
    }
}