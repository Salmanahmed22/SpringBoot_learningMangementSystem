package com.example.demo.service;

import com.example.demo.dtos.AssignmentDTO;
import com.example.demo.dtos.CourseDTO;
import com.example.demo.models.*;
import com.example.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    @Lazy
    private InstructorService instructorService;

    @Autowired
    @Lazy
    private LessonService lessonService;

    @Autowired
    @Lazy
    private QuizService quizService;

    @Autowired
    @Lazy
    private AssignmentService assignmentService;

    @Autowired
    @Lazy
    private StudentService studentService;



    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course createCourse(Instructor instructor, CourseDTO courseDTO) {
        Course course = new Course();
        if(courseDTO.getMinLevel() != 0)
            course.setMinLevel(courseDTO.getMinLevel());
        if(courseDTO.getTitle() != null)
            course.setTitle(courseDTO.getTitle());
        if(courseDTO.getDescription() != null)
            course.setDescription(courseDTO.getDescription());

        course.getQuestionBank().setCourse(course);
        course.setInstructor(instructor);
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, CourseDTO updatedCourse) {
        Course existingCourse = courseRepository.findById(id).orElse(null);
        if (existingCourse != null) {
            if(updatedCourse.getMinLevel() != 0)
                existingCourse.setMinLevel(updatedCourse.getMinLevel());
            if(updatedCourse.getTitle() != null)
                existingCourse.setTitle(updatedCourse.getTitle());
            if(updatedCourse.getDescription() != null)
                existingCourse.setDescription(updatedCourse.getDescription());
            return courseRepository.save(existingCourse);
        }
        return null;
    }

    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        List<Lesson> lessons = course.getLessons();
        for(Lesson lesson : lessons){
            lessonService.deleteLesson(lesson.getId());
        }
        List<Assignment> assignments = course.getAssignments();
        for(Assignment assignment : assignments){
            assignmentService.deleteAssignment(assignment.getId());
        }
        List<Quiz> quizes = course.getQuizzes();
        for(Quiz quiz : quizes){
            quizService.deleteQuiz(quiz.getId());
        }
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

    public Course addAssignment(AssignmentDTO assignmentDTO) {
        Course course = getCourseById(assignmentDTO.getCourseId());
        Assignment newAssignment = assignmentService.createAssignment(assignmentDTO);
        List<Assignment> courseAssignments = course.getAssignments();
        if (!courseAssignments.contains(newAssignment)) {
            courseAssignments.add(newAssignment);
            course.setAssignments(courseAssignments);
        }

        return courseRepository.save(course);
    }
}
