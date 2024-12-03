package com.stackoverflow.service;

import com.stackoverflow.dto.QuestionRequestDTO;
import com.stackoverflow.model.Question;

import java.util.List;

public interface QuestionService {
    Question createQuestion(QuestionRequestDTO questionRequestDTO);
    Question getQuestionById(Long id);
    List<Question> getAllQuestions();
    void updateQuestion(Long id, Question question);
    void deleteQuestion(Long id);
//    List<Question> searchQuestions(String title, String tag);
//    List<Question> filterQuestionsByStatus(String status);
}
