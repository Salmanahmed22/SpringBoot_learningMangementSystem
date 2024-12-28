package com.example.demo.serviceTest;

import com.example.demo.dtos.LessonDTO;
import com.example.demo.models.Course;
import com.example.demo.models.Lesson;
import com.example.demo.repository.LessonRepository;
import com.example.demo.service.CourseService;
import com.example.demo.service.LessonService;
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
public class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private LessonService lessonService;

    private Lesson testLesson;
    private LessonDTO testLessonDTO;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Test Course");

        testLesson = new Lesson();
        testLesson.setId(1L);
        testLesson.setTitle("Test Lesson");
        testLesson.setContent("Test Content");
        testLesson.setCourse(testCourse);

        testLessonDTO = new LessonDTO();
        testLessonDTO.setTitle("Test Lesson");
        testLessonDTO.setContent("Test Content");
        testLessonDTO.setCourseId(1L);
    }

    @Test
    void getLessonById_Success() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(testLesson));

        Lesson result = lessonService.getLessonById(1L);

        assertNotNull(result);
        assertEquals(testLesson.getTitle(), result.getTitle());
        assertEquals(testLesson.getContent(), result.getContent());
        verify(lessonRepository).findById(1L);
    }

    @Test
    void getLessonById_NotFound() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        Lesson result = lessonService.getLessonById(1L);

        assertNull(result);
        verify(lessonRepository).findById(1L);
    }

    @Test
    void getAllLessons_Success() {
        List<Lesson> lessons = Arrays.asList(testLesson);
        when(lessonRepository.findAll()).thenReturn(lessons);

        List<Lesson> result = lessonService.getAllLessons();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testLesson.getTitle(), result.get(0).getTitle());
        verify(lessonRepository).findAll();
    }

    @Test
    void createLesson_FromDTO_Success() {
        when(courseService.getCourseById(1L)).thenReturn(testCourse);
        when(lessonRepository.save(any(Lesson.class))).thenReturn(testLesson);

        Lesson result = lessonService.createLesson(testLessonDTO);

        assertNotNull(result);
        assertEquals(testLesson.getTitle(), result.getTitle());
        assertEquals(testLesson.getContent(), result.getContent());
        verify(lessonRepository).save(any(Lesson.class));
        verify(courseService).getCourseById(1L);
    }

    @Test
    void createLesson_FromLesson_Success() {
        when(lessonRepository.save(any(Lesson.class))).thenReturn(testLesson);

        Lesson result = lessonService.createLesson(testLesson);

        assertNotNull(result);
        assertEquals(testLesson.getTitle(), result.getTitle());
        assertEquals(testLesson.getContent(), result.getContent());
        verify(lessonRepository).save(testLesson);
    }

    @Test
    void updateLesson_Success() {
        Lesson updatedLesson = new Lesson();
        updatedLesson.setTitle("Updated Title");
        updatedLesson.setContent("Updated Content");
        updatedLesson.setCourse(testCourse);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(testLesson));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(updatedLesson);

        Lesson result = lessonService.updateLesson(1L, updatedLesson);

        assertNotNull(result);
        assertEquals(updatedLesson.getTitle(), result.getTitle());
        assertEquals(updatedLesson.getContent(), result.getContent());
        verify(lessonRepository).save(any(Lesson.class));
    }

    @Test
    void updateLesson_NotFound() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        Lesson result = lessonService.updateLesson(1L, testLesson);

        assertNull(result);
        verify(lessonRepository).findById(1L);
        verify(lessonRepository, never()).save(any(Lesson.class));
    }

    @Test
    void deleteLesson_Success() {
        doNothing().when(lessonRepository).deleteById(1L);

        lessonService.deleteLesson(1L);

        verify(lessonRepository).deleteById(1L);
    }

    @Test
    void saveLesson_Success() {
        when(lessonRepository.save(any(Lesson.class))).thenReturn(testLesson);

        lessonService.saveLesson(testLesson);

        verify(lessonRepository).save(testLesson);
    }


    @Test
    void updateLesson_WithNullFields() {
        Lesson updatedLesson = new Lesson();
        // Don't set any fields

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(testLesson));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(testLesson);

        Lesson result = lessonService.updateLesson(1L, updatedLesson);

        assertNotNull(result);
        verify(lessonRepository).save(any(Lesson.class));
    }
}
