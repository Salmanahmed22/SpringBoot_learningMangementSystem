package com.example.demo.service;

import java.util.*;

import com.example.demo.dtos.QuizDTO;
import com.example.demo.models.*;
import com.example.demo.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private CourseService courseService;


    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id).orElse(null);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Quiz createQuiz(Course course, QuizDTO quizDTO) {
        Quiz quiz = new Quiz();
        quiz.setTitle(quizDTO.getTitle());
        quiz.setCourse(course);
        quiz.setDescription(quizDTO.getDescription());
        quiz.setQuizDate(quizDTO.getQuizDate());
        quiz.setDuration(quizDTO.getDuration());
        quiz.setNumOfQuestions(quizDTO.getNumOfQuestions());

        QuestionBank questionBank = course.getQuestionBank();

        if (quiz.getNumOfQuestions() > questionBank.getQuestions().size()) {
            throw new IllegalStateException("Not enough questions (in question bank) to create the quiz.");
        }

        List<Question> questions = questionBank.getQuestions();
        Collections.shuffle(questions);
        List<Question> selectedQuestions = questions.subList(0, quiz.getNumOfQuestions());

        quiz.setQuestions(selectedQuestions);

        return quizRepository.save(quiz);
    }

    public String submitQuiz(Student student, Long quizId, Submission submission) {
        Quiz quiz = getQuizById(quizId);

        String grade = calculateGrades(quiz, submission);

        Map<Long, String> submissionGrades = student.getQuizGrades();
        submissionGrades.put(quizId, grade);
        student.setQuizGrades(submissionGrades);

        List<Submission> submissions = quiz.getSubmissions();
        submission.setGrade(grade);
        submissions.add(submission);

        quiz.setSubmissions(submissions);
        quizRepository.save(quiz);
        studentService.saveStudent(student);
        return grade;
    }

    public String calculateGrades(Quiz quiz ,Submission submission) {
        List<Question> questions = quiz.getQuestions();
        List<String> answers = submission.getAnswers();
        int grade = 0;
        for(int i = 0; i < answers.size(); i++)
        {
            if(Objects.equals(answers.get(i), questions.get(i).getCorrectAnswer()))
            {
                grade++;
            }
        }
        return grade + " / " + questions.size();
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
            questionService.deleteQuestion(question.getId());
        }
        List<Submission> submissions = quiz.getSubmissions();
        for(Submission submission : submissions){
            submissionService.deleteSubmission(submission.getId());
        }
        quizRepository.deleteById(id);
    }

//    // Method to retrieve a student's submission
//    public List<Answer> getSubmission(Long studentId) {
//        return submissions.get(studentId);
//    }
}
