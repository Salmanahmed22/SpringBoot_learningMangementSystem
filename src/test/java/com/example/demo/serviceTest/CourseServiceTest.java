package com.example.demo.serviceTest;

import com.example.demo.dtos.AssignmentDTO;
import com.example.demo.dtos.CourseDTO;
import com.example.demo.models.*;
import com.example.demo.repository.CourseRepository;
import com.example.demo.service.*;
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
public class CourseServiceTest {

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

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private CourseService courseService;

    private Course testCourse;
    private CourseDTO testCourseDTO;
    private Instructor testInstructor;
    private Lesson testLesson;
    private Assignment testAssignment;
    private Quiz testQuiz;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        testInstructor = new Instructor();
        testInstructor.setId(1L);
        testInstructor.setName("Test Instructor");

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Test Course");
        testCourse.setDescription("Test Description");
        testCourse.setMinLevel((short) 1);
        testCourse.setInstructor(testInstructor);
        testCourse.setQuestionBank(new QuestionBank());
        testCourse.setLessons(new ArrayList<>());
        testCourse.setAssignments(new ArrayList<>());
        testCourse.setQuizzes(new ArrayList<>());
        testCourse.setEnrolledStudents(new ArrayList<>());

        testCourseDTO = new CourseDTO();
        testCourseDTO.setTitle("Test Course");
        testCourseDTO.setDescription("Test Description");
        testCourseDTO.setMinLevel((short) 1);
        testCourseDTO.setInstructorId(1L);

        testLesson = new Lesson();
        testLesson.setId(1L);
        testLesson.setTitle("Test Lesson");

        testAssignment = new Assignment();
        testAssignment.setId(1L);
        testAssignment.setTitle("Test Assignment");

        testQuiz = new Quiz();
        testQuiz.setId(1L);
        testQuiz.setTitle("Test Quiz");

        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setName("Test Student");
    }

    @Test
    void getCourseById_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        Course result = courseService.getCourseById(1L);

        assertNotNull(result);
        assertEquals(testCourse.getTitle(), result.getTitle());
        verify(courseRepository).findById(1L);
    }

    @Test
    void getCourseById_NotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Course result = courseService.getCourseById(1L);

        assertNull(result);
        verify(courseRepository).findById(1L);
    }

    @Test
    void getAllCourses_Success() {
        List<Course> courses = Arrays.asList(testCourse);
        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> result = courseService.getAllCourses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCourse.getTitle(), result.get(0).getTitle());
        verify(courseRepository).findAll();
    }

    @Test
    void createCourse_Success() {
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        Course result = courseService.createCourse(testInstructor, testCourseDTO);

        assertNotNull(result);
        assertEquals(testCourse.getTitle(), result.getTitle());
        assertEquals(testCourse.getDescription(), result.getDescription());
        assertEquals(testCourse.getMinLevel(), result.getMinLevel());
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void updateCourse_Success() {
        CourseDTO updatedCourseDTO = new CourseDTO();
        updatedCourseDTO.setTitle("Updated Title");
        updatedCourseDTO.setDescription("Updated Description");
        updatedCourseDTO.setMinLevel((short) 2);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        Course result = courseService.updateCourse(1L, updatedCourseDTO);

        assertNotNull(result);
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void updateCourse_NotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Course result = courseService.updateCourse(1L, testCourseDTO);

        assertNull(result);
        verify(courseRepository).findById(1L);
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void deleteCourse_Success() {
        testCourse.setLessons(Arrays.asList(testLesson));
        testCourse.setAssignments(Arrays.asList(testAssignment));
        testCourse.setQuizzes(Arrays.asList(testQuiz));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        doNothing().when(lessonService).deleteLesson(any());
        doNothing().when(assignmentService).deleteAssignment(any());
        doNothing().when(quizService).deleteQuiz(any());
        doNothing().when(courseRepository).deleteById(any());

        courseService.deleteCourse(1L);

        verify(lessonService).deleteLesson(1L);
        verify(assignmentService).deleteAssignment(1L);
        verify(quizService).deleteQuiz(1L);
        verify(courseRepository).deleteById(1L);
    }

    @Test
    void addLesson_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(lessonService.createLesson(any(Lesson.class))).thenReturn(testLesson);
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        Course result = courseService.addLesson(1L, testLesson);

        assertNotNull(result);
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void removeStudentFromCourse_Success() {
        testCourse.getEnrolledStudents().add(testStudent);

        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);
        doNothing().when(studentService).removeEnrolledCourse(any(Course.class), any(Student.class));

        courseService.removeStudentFromCourse(testCourse, testStudent);

        verify(courseRepository).save(testCourse);
        verify(studentService).removeEnrolledCourse(testCourse, testStudent);
    }

    @Test
    void addAssignment_Success() {
        AssignmentDTO assignmentDTO = new AssignmentDTO();
        assignmentDTO.setCourseId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(assignmentService.createAssignment(any(AssignmentDTO.class))).thenReturn(testAssignment);
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        Course result = courseService.addAssignment(assignmentDTO);

        assertNotNull(result);
        verify(courseRepository).save(any(Course.class));
    }
}