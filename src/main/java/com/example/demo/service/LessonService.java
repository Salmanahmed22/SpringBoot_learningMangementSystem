package com.example.demo.service;

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
    private StudentRepository studentRepository;

    public Lesson getLessonById(Long id) {
        return lessonRepository.findById(id).orElse(null);
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
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



    public String getLessonContent(Long studentId, Long courseId,Long lessonId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        return student.getLessonContent(courseId ,lessonId);
    }

    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }
}
