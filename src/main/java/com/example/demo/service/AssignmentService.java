package com.example.demo.service;

import com.example.demo.dtos.AssignmentDTO;
import com.example.demo.models.Assignment;
import com.example.demo.models.Course;
import com.example.demo.models.Lesson;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private CourseService courseService;

    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id).orElse(null);
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public Assignment createAssignment(AssignmentDTO assignmentDTO) {
        Assignment assignment = new Assignment();
        assignment.setTitle(assignmentDTO.getTitle());
        assignment.setMark(assignmentDTO.getMark());
        assignment.setDescription(assignmentDTO.getDescription());
        assignment.setDueDate(assignmentDTO.getDueDate());
        assignment.setCourse(courseService.getCourseById(assignmentDTO.getCourseId())) ;
        return assignmentRepository.save(assignment);


    }

    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public Assignment updateAssignment(Long id, Assignment updatedAssignment) {
        // Fetch the existing assignment by ID
        Assignment existingAssignment = assignmentRepository.findById(id).orElse(null);
        if (existingAssignment != null) {
            if (updatedAssignment.getTitle() != null) {
                existingAssignment.setTitle(updatedAssignment.getTitle());
            }
            if (updatedAssignment.getDescription() != null) {
                existingAssignment.setDescription(updatedAssignment.getDescription());
            }
            if (updatedAssignment.getDueDate() != null) {
                existingAssignment.setDueDate(updatedAssignment.getDueDate());
            }
            if (updatedAssignment.getCourse() != null) {
                existingAssignment.setCourse(updatedAssignment.getCourse());
            }
            return assignmentRepository.save(existingAssignment);
        }
        return null;
    }


    public Assignment submitAssignment(Long assignmentId, Long studentId,String submissionContent) {
        Assignment existingAssignment = assignmentRepository.findById(assignmentId).orElse(null);
        if (existingAssignment != null) {
            Map<Long, String> submissions = existingAssignment.getSubmissions();
            submissions.put(studentId, submissionContent);
            existingAssignment.setSubmissions(submissions);
            return assignmentRepository.save(existingAssignment);

        }
        return null;
    }
//    public void submitAssignment(Long studentId, Long assignmentId, String submissionContent) {
//        // Find the student by ID
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
//
//        // Call the method in the Student class to submit the assignment
//        student.submitAssignment(assignmentId, submissionContent);
//
//        // The assignment is automatically updated through the Student object.
//        // No need to explicitly save the assignment, because it’s updated through JPA.
//    }

    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }
}
