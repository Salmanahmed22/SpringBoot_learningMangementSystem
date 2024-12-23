package com.example.demo.service;

import com.example.demo.dtos.QuestionDTO;
import com.example.demo.models.Question;
import com.example.demo.models.QuestionBank;
import com.example.demo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    public Question createQuestion(QuestionBank questionBank, QuestionDTO questionDTO) {
       Question question = new Question();
       question.setQuestionBank(questionBank);
       question.setQuestion(questionDTO.getQuestion());
       question.setOptions(questionDTO.getOptions());
       question.setCorrectAnswer(questionDTO.getCorrectAnswer());
       return question;
    }


    public Question updateQuestion(Long id, Question updatedQuestion) {
        Question existingQuestion = questionRepository.findById(id).orElse(null);
        if (existingQuestion != null) {
            existingQuestion.setQuestion(updatedQuestion.getQuestion());
            existingQuestion.setQuestionBank(updatedQuestion.getQuestionBank());
            existingQuestion.setCorrectAnswer(updatedQuestion.getCorrectAnswer());
            return questionRepository.save(existingQuestion);
        }
        return null;
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}