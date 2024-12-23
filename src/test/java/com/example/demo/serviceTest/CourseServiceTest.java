
package com.example.demo.serviceTest;

import com.example.demo.dtos.CourseDTO;
import com.example.demo.models.*;
import com.example.demo.repository.CourseRepository;
import com.example.demo.service.*;
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
public class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private InstructorService instructorService;

    @Mock
    private LessonService lessonService;

    @Mock
    private QuizService quizService;

    @Mock
    private AssignmentService assignmentService;

    @Mock
    private StudentService studentService;

    private Course course;
    private Instructor instructor;
    private CourseDTO courseDTO;
    private Lesson lesson;
    private Assignment assignment;

    @BeforeEach
    public void setUp() {
        // Set up mock data
        instructor = new Instructor();
        instructor.setId(1L);
        instructor.setName("Instructor Name");

        course = new Course();
        course.setId(1L);
        course.setTitle("Java Programming");
        course.setDescription("Java basics course");
        course.setMinLevel((short) 1);
        course.setInstructor(instructor);

        courseDTO = new CourseDTO();
        courseDTO.setTitle("Java Programming");
        courseDTO.setDescription("Java basics course");
        courseDTO.setMinLevel((short) 1);

        lesson = new Lesson();
        lesson.setId(1L);
        lesson.setTitle("Lesson 1");

        assignment = new Assignment();
        assignment.setId(1L);
        assignment.setTitle("Assignment 1");
    }

    @Test
    public void testGetCourseById_Found() {
        // Arrange
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Act
        Course result = courseService.getCourseById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Java Programming", result.getTitle());
    }

    @Test
    public void testGetCourseById_NotFound() {
        // Arrange
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Course result = courseService.getCourseById(1L);

        // Assert
        assertNull(result);
    }

    @Test
    public void testCreateCourse() {
        // Arrange
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // Act
        Course result = courseService.createCourse(instructor, courseDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Java Programming", result.getTitle());
        assertEquals(instructor, result.getInstructor());
    }

    @Test
    public void testUpdateCourse_Success() {
        // Arrange
        CourseDTO updatedCourseDTO = new CourseDTO();
        updatedCourseDTO.setTitle("Advanced Java Programming");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // Act
        Course result = courseService.updateCourse(1L, updatedCourseDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Advanced Java Programming", result.getTitle());
    }

    @Test
    public void testUpdateCourse_NotFound() {
        // Arrange
        CourseDTO updatedCourseDTO = new CourseDTO();
        updatedCourseDTO.setTitle("Advanced Java Programming");

        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Course result = courseService.updateCourse(1L, updatedCourseDTO);

        // Assert
        assertNull(result);
    }

    @Test
    public void testDeleteCourse() {
        // Arrange
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        doNothing().when(lessonService).deleteLesson(anyLong());
        doNothing().when(assignmentService).deleteAssignment(anyLong());
        doNothing().when(quizService).deleteQuiz(anyLong());
        doNothing().when(courseRepository).deleteById(anyLong());

        // Act
        courseService.deleteCourse(1L);

        // Assert
        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testAddLesson() {
        // Arrange
        when(lessonService.createLesson(any(Course.class), any(Lesson.class))).thenReturn(lesson);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // Act
        Course result = courseService.addLesson(course, lesson);

        // Assert
        assertNotNull(result);
        assertTrue(result.getLessons().contains(lesson));
    }

    @Test
    public void testRemoveStudentFromCourse() {
        // Arrange
        Student student = new Student();
        student.setId(1L);
        student.setName("Student Name");

        course.getEnrolledStudents().add(student);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // Act
        courseService.removeStudentFromCourse(course, student);

        // Assert
        assertFalse(course.getEnrolledStudents().contains(student));
        verify(studentService, times(1)).removeEnrolledCourse(course, student);
    }

    @Test
    public void testAddAssignment() {
        // Arrange
        when(assignmentService.createAssignment(any(Course.class), any(Assignment.class))).thenReturn(assignment);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // Act
        Course result = courseService.addAssignment(course, assignment);

        // Assert
        assertNotNull(result);
        assertTrue(result.getAssignments().contains(assignment));
    }
}
