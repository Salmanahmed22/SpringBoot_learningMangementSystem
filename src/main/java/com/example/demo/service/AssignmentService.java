package com.example.demo.service;

import com.example.demo.models.Assignment;
import com.example.demo.models.Student;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id).orElse(null);
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public Assignment updateAssignment(Long id, Assignment updatedAssignment) {
        Assignment existingAssignment = assignmentRepository.findById(id).orElse(null);
        if (existingAssignment != null) {
            existingAssignment.setTitle(updatedAssignment.getTitle());
            existingAssignment.setDescription(updatedAssignment.getDescription());
            existingAssignment.setDueDate(updatedAssignment.getDueDate());
            existingAssignment.setCourse(updatedAssignment.getCourse());
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
