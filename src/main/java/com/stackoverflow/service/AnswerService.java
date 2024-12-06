package com.stackoverflow.service;

import com.stackoverflow.model.Answer;

import java.util.List;

public interface AnswerService {
    List<Answer> findAnswersByQuestionId(long questionId);
    Answer findAnswerById(long id);
    Answer createAnswer(Answer answer);
    void updateAnswer(Answer updatedAnswer);
    void deleteAnswerById(long id);
    List<Answer> findAllAnswers();
    void upvoteAnswer(Long id);
    void downvoteAnswer(Long id);
}