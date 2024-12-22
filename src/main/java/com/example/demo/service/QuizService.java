package com.example.demo.service;

import com.example.demo.models.Question;
import com.example.demo.models.Quiz;
import com.example.demo.models.Submission;
import com.example.demo.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionBankService questionBankService;


    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id).orElse(null);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public Quiz submitQuiz(Long quizId, Submission submission) {
        Quiz quiz = getQuizById(quizId);
        List<Submission> submissions = quiz.getSubmissions();
        submissions.add(submission);
        quiz.setSubmissions(submissions);
        return quizRepository.save(quiz);
    }

    public Quiz updateQuiz(Long id, Quiz updatedQuiz) {
        Quiz existingQuiz = quizRepository.findById(id).orElse(null);
        if (existingQuiz != null) {
            existingQuiz.setTitle(updatedQuiz.getTitle());
            existingQuiz.setDescription(updatedQuiz.getDescription());
            existingQuiz.setCourse(updatedQuiz.getCourse());
            return quizRepository.save(existingQuiz);
        }
        return null;
    }

    public void deleteQuiz(Long id) {
        Quiz quiz = getQuizById(id);
        List<Question> questions = quiz.getQuestions();
        for(Question question : questions){

        }
        quizRepository.deleteById(id);
    }

//    // Method to retrieve a student's submission
//    public List<Answer> getSubmission(Long studentId) {
//        return submissions.get(studentId);
//    }
}
