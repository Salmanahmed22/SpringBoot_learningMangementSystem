package com.example.demo.service;


import com.example.demo.models.Question;
import com.example.demo.models.QuestionBank;
import com.example.demo.repository.QuestionBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionBankService {

    @Autowired
    private QuestionBankRepository questionBankRepository;

    public void addQuestion(Long QuestionBankId, Question question) {
        QuestionBank questionBank = questionBankRepository.findById(QuestionBankId)
                .orElseThrow(() -> new IllegalArgumentException("Question Bank not found"));
        List<Question> newQuestions = questionBank.getQuestions();
        newQuestions.add(question);
        questionBank.setQuestions(newQuestions);
    }

    public void removeQuestion(Question question) {
        questionBankRepository.deleteById(question.getId());
    }

}
