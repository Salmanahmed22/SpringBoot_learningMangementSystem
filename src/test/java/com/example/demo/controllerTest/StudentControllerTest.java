package com.example.demo.controllerTest;

import com.example.demo.controller.StudentController;
import com.example.demo.dtos.AssignmentSubmissionDTO;
import com.example.demo.dtos.StudentDTO;
import com.example.demo.dtos.SubmissionDTO;
import com.example.demo.models.*;
import com.example.demo.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Student testStudent;
    private Course testCourse;
    private Assignment testAssignment;
    private Quiz testQuiz;
    private Lesson testLesson;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setName("Test Student");

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Test Course");

        testAssignment = new Assignment();
        testAssignment.setId(1L);
        testAssignment.setTitle("Test Assignment");

        testQuiz = new Quiz();
        testQuiz.setId(1L);
        testQuiz.setTitle("Test Quiz");

        testLesson = new Lesson();
        testLesson.setId(1L);
        testLesson.setTitle("Test Lesson");
    }

    @Test
    void createStudent_ShouldReturnCreatedStudent() throws Exception {
        when(studentService.createStudent(any(Student.class))).thenReturn(testStudent);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testStudent.getId()))
                .andExpect(jsonPath("$.name").value(testStudent.getName()));
    }

    @Test
    void getStudentById_ShouldReturnStudent() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(testStudent);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testStudent.getId()))
                .andExpect(jsonPath("$.name").value(testStudent.getName()));
    }

    @Test
    void getAllStudents_ShouldReturnListOfStudents() throws Exception {
        List<Student> students = Arrays.asList(testStudent);
        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testStudent.getId()))
                .andExpect(jsonPath("$[0].name").value(testStudent.getName()));
    }

    @Test
    void enrollCourse_ShouldReturnUpdatedStudent() throws Exception {
        when(studentService.enrollCourse(1L, 1L)).thenReturn(testStudent);

        mockMvc.perform(put("/api/students/1/enroll/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testStudent.getId()));
    }

    @Test
    void viewAvailableCourses_ShouldReturnCoursesList() throws Exception {
        List<Course> courses = Arrays.asList(testCourse);
        when(studentService.getAvailableCourses(1L)).thenReturn(courses);

        mockMvc.perform(get("/api/students/1/available-courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testCourse.getId()))
                .andExpect(jsonPath("$[0].title").value(testCourse.getTitle()));
    }

//    @Test
//    void submitAssignment_ShouldReturnSuccess() throws Exception {
//        AssignmentSubmissionDTO submission = new AssignmentSubmissionDTO();
//        submission.setSubmissionContent("Test submission");
//
//        mockMvc.perform(post("/api/students/1/assignments/1/submit")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(submission)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Assignment submitted successfully"));
//    }

    @Test
    void submitQuiz_ShouldReturnGrade() throws Exception {
        SubmissionDTO submission = new SubmissionDTO();
        when(studentService.takeQuiz(eq(1L), eq(1L), any(SubmissionDTO.class))).thenReturn(0.85);

        mockMvc.perform(post("/api/students/1/quizzes/1/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(submission)))
                .andExpect(status().isOk())
                .andExpect(content().string("Grade 85.0%"));
    }

//    @Test
//    void editStudentProfile_ShouldReturnUpdatedStudent() throws Exception {
//        StudentDTO studentDTO = new StudentDTO();
//        studentDTO.setName("Updated Name");
//
//        when(studentService.updateStudentProfile(eq(1L), any(StudentDTO.class))).thenReturn(testStudent);
//
//        mockMvc.perform(put("/api/students/1/editProfile")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(studentDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(testStudent.getId()));
//    }

    @Test
    void unrollCourse_ShouldReturnUpdatedStudent() throws Exception {
        when(studentService.unrollCourse(1L, 1L)).thenReturn(testStudent);

        mockMvc.perform(put("/api/students/1/unroll/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testStudent.getId()));
    }

    @Test
    void deleteStudent_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());
    }
}