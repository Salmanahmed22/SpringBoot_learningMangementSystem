package com.example.demo.service;

import com.example.demo.dtos.SubmissionDTO;
import com.example.demo.models.Submission;
import com.example.demo.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private QuestionService questionService;

    public Submission getSubmissionById(Long id) {
        return submissionRepository.findById(id).orElse(null);
    }

    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    public Submission createSubmission(Submission submission) {
        return submissionRepository.save(submission);
    }


    public Submission updateSubmission(Long id, Submission updatedSubmission) {
        Submission existingSubmission = submissionRepository.findById(id).orElse(null);
        if (existingSubmission != null) {
            existingSubmission.setAnswers(updatedSubmission.getAnswers());
            existingSubmission.setQuiz(updatedSubmission.getQuiz());
            existingSubmission.setStudent(updatedSubmission.getStudent());
            existingSubmission.setGrade(updatedSubmission.getGrade());
            return submissionRepository.save(existingSubmission);
        }
        return null;
    }

    public void deleteSubmission(Long id) {
        submissionRepository.deleteById(id);
    }
}
