package com.example.demo.service;

import com.example.demo.models.Course;
import com.example.demo.models.Instructor;
import com.example.demo.models.Lesson;
import com.example.demo.models.Student;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.InstructorRepository;
import jdk.jfr.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private LessonService lessonService;
    @Autowired
    @Lazy
    private StudentService studentService;

    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course createCourse(Course course) {
        if (course.getInstructor() != null) {
            Long instructorId = course.getInstructor().getId();
            Instructor instructor = instructorRepository.findById(instructorId)
                    .orElseThrow(() -> new IllegalArgumentException("Instructor not found with id: " + instructorId));
            course.setInstructor(instructor);
        }
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course updatedCourse) {
        Course existingCourse = courseRepository.findById(id).orElse(null);
        if (existingCourse != null) {
            existingCourse.setTitle(updatedCourse.getTitle());
            existingCourse.setDescription(updatedCourse.getDescription());
            return courseRepository.save(existingCourse);
        }
        return null;
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public Course addLesson(Long courseId, Lesson lesson) {
        Course course = courseRepository.findById(courseId).orElseThrow(null);
        List <Lesson> courseLessons = course.getLessons();
        courseLessons.add(lesson);
        course.setLessons(courseLessons);
        return courseRepository.save(course);
    }

    public void removeStudentFromCourse(Course course, Student student) {
        course.getEnrolledStudents().remove(student);
        courseRepository.save(course);
        studentService.removeEnrolledCourse(course, student);
    }
}
