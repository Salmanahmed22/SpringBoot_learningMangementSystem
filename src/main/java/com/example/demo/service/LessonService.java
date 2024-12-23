package com.example.demo.service;

import com.example.demo.dtos.LessonDTO;
import com.example.demo.models.Lesson;
import com.example.demo.models.Student;
import com.example.demo.repository.LessonRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CourseService courseService;

    public Lesson getLessonById(Long id) {
        return lessonRepository.findById(id).orElse(null);
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    public Lesson createLesson(LessonDTO lessonDTO) {
        Lesson lesson = new Lesson();
        lesson.setTitle(lessonDTO.getTitle());
        lesson.setContent(lessonDTO.getContent());
        lesson.setCourse(courseService.getCourseById(lessonDTO.getCourseId()));
        return lessonRepository.save(lesson);
    }

    public Lesson createLesson(Lesson lesson) {
        return lessonRepository.save(lesson);

    }

    public Lesson updateLesson(Long id, Lesson updatedLesson) {
        Lesson existingLesson = lessonRepository.findById(id).orElse(null);
        if (existingLesson != null) {
            existingLesson.setTitle(updatedLesson.getTitle());
            existingLesson.setContent(updatedLesson.getContent());
            existingLesson.setCourse(updatedLesson.getCourse());
            return lessonRepository.save(existingLesson);
        }
        return null;
    }

    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }
}
