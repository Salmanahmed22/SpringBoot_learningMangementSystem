package com.example.demo.service;

import com.example.demo.DTO.CourseRequest;
import com.example.demo.models.Course;
import com.example.demo.models.Instructor;
import com.example.demo.models.Lesson;
import com.example.demo.models.Student;
import com.example.demo.repository.CourseRepository;
import com.example.demo.service.InstructorService;
import jdk.jfr.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    @Lazy
    private InstructorService instructorService;

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

    public Course createCourse(CourseRequest courseRequest) {
        Course course = new Course();
        course.setMinLevel(courseRequest.getMinLevel());
        course.setTitle(courseRequest.getTitle());
        course.setDescription(courseRequest.getDescription());
        Instructor instructor = instructorService.getInstructorById(courseRequest.getInstructorId());
        if (instructor != null) {
            course.setInstructor(instructor);
        }
        else throw new RuntimeException("instructor not found");

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
        Lesson newLesson = lessonService.createLesson(lesson);
        List <Lesson> courseLessons = course.getLessons();
        if(!courseLessons.contains(newLesson)) {
            courseLessons.add(newLesson);
            course.setLessons(courseLessons);
        }
        return courseRepository.save(course);
    }

    public void removeStudentFromCourse(Course course, Student student) {
        course.getEnrolledStudents().remove(student);
        courseRepository.save(course);
        studentService.removeEnrolledCourse(course, student);
    }
}
