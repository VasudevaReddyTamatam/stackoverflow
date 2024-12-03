package com.stackoverflow.service;

import com.stackoverflow.repository.AnswerRepository;
import com.stackoverflow.model.Answer;
import com.stackoverflow.repository.QuestionRepository;
import com.stackoverflow.model.Question;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserService userService;

    AnswerServiceImpl(AnswerRepository answerRepository, QuestionRepository questionRepository,UserService userService) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.userService=userService;
    }

    @Override
    public List<Answer> findAnswersByQuestionId(long questionId) {
        return answerRepository.findByQuestionId(questionId);
    }

    @Override
    public Answer findAnswerById(long id) {
        return answerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Answer not found"));
    }

    @Override
    public Answer createAnswer(Answer answer) {
        Question question = questionRepository.findById(answer.getQuestion().getId())
                .orElseThrow(() -> new IllegalArgumentException("Question not found for creating the answer."));
        answer.setQuestion(question);
        answer.setCreatedAt(LocalDateTime.now());
        answer.setUpdatedAt(LocalDateTime.now());
        answer.setUser(userService.getUserById(1L));
        return answerRepository.save(answer);
    }

    @Override
    public void updateAnswer(Answer updatedAnswer) {
        updatedAnswer.setUpdatedAt(LocalDateTime.now());
        answerRepository.save(updatedAnswer);
    }

    @Override
    public void deleteAnswerById(long id) {
        answerRepository.deleteById(id);
    }

    @Override
    public List<Answer> findAllAnswers() {
        return answerRepository.findAll();
    }
}