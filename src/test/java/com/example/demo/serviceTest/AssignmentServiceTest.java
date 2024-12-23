
package com.example.demo.serviceTest;

import com.example.demo.models.Assignment;
import com.example.demo.dtos.AssignmentDTO;
import com.example.demo.models.Course;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.AssignmentService;
import com.example.demo.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AssignmentServiceTest {

    @InjectMocks
    private AssignmentService assignmentService;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseService courseService;

    private Assignment assignment;
    private Course course;

    @BeforeEach
    public void setUp() {
        // Set up mock data
        course = new Course();
        course.setId(1L);
        course.setTitle("Java Programming");

        assignment = new Assignment();
        assignment.setId(1L);
        assignment.setTitle("Assignment 1");
        assignment.setDescription("Java Basics");
        assignment.setCourse(course);
    }

    @Test
    public void testGetAssignmentById_Found() {
        // Arrange
        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));

        // Act
        Assignment result = assignmentService.getAssignmentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Assignment 1", result.getTitle());
    }

    @Test
    public void testGetAssignmentById_NotFound() {
        // Arrange
        when(assignmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Assignment result = assignmentService.getAssignmentById(1L);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetAllAssignments() {
        // Arrange
        when(assignmentRepository.findAll()).thenReturn(Arrays.asList(assignment));

        // Act
        List<Assignment> result = assignmentService.getAllAssignments();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    public void testCreateAssignment() {
        // Arrange
        when(assignmentRepository.save(assignment)).thenReturn(assignment);

        AssignmentDTO assignmentDTO = new AssignmentDTO();
        assignmentDTO.setTitle(assignment.getTitle());
        assignmentDTO.setDescription(assignment.getDescription());
        assignmentDTO.setMark(assignment.getMark());
        assignmentDTO.setDueDate(assignment.getDueDate());
        assignmentDTO.setSubmissions(assignment.getSubmissions());
        assignmentDTO.setCourseId(assignment.getCourse().getId());
        // Act
        Assignment result = assignmentService.createAssignment(assignmentDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Assignment 1", result.getTitle());
        assertEquals(course, result.getCourse());
    }

    @Test
    public void testUpdateAssignment_Success() {
        // Arrange
        Assignment updatedAssignment = new Assignment();
        updatedAssignment.setTitle("Updated Title");
        updatedAssignment.setDescription("Updated Description");

        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(updatedAssignment);

        // Act
        Assignment result = assignmentService.updateAssignment(1L, updatedAssignment);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
    }

    @Test
    public void testUpdateAssignment_NotFound() {
        // Arrange
        Assignment updatedAssignment = new Assignment();
        updatedAssignment.setTitle("Updated Title");

        when(assignmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Assignment result = assignmentService.updateAssignment(1L, updatedAssignment);

        // Assert
        assertNull(result);
    }

    @Test
    public void testSubmitAssignment_Success() {
        // Arrange
        Long studentId = 123L;
        String submissionContent = "My submission content";
        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

        // Act
        Assignment result = assignmentService.submitAssignment(1L, studentId, submissionContent);

        // Assert
        assertNotNull(result);
        assertTrue(result.getSubmissions().containsKey(studentId));
        assertEquals(submissionContent, result.getSubmissions().get(studentId));
    }

    @Test
    public void testSubmitAssignment_NotFound() {
        // Arrange
        Long studentId = 123L;
        String submissionContent = "My submission content";
        when(assignmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Assignment result = assignmentService.submitAssignment(1L, studentId, submissionContent);

        // Assert
        assertNull(result);
    }

    @Test
    public void testDeleteAssignment() {
        // Arrange
        doNothing().when(assignmentRepository).deleteById(1L);

        // Act
        assignmentService.deleteAssignment(1L);

        // Assert
        verify(assignmentRepository, times(1)).deleteById(1L);
    }
}
