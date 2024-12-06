package com.stackoverflow.service;

import com.stackoverflow.constants.ActionPoints;
import com.stackoverflow.model.User;
import com.stackoverflow.repository.AnswerRepository;
import com.stackoverflow.model.Answer;
import com.stackoverflow.repository.QuestionRepository;
import com.stackoverflow.model.Question;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserService userService;
    private final VotingService votingService;

    AnswerServiceImpl(AnswerRepository answerRepository, QuestionRepository questionRepository,
                      UserService userService,VotingService votingService) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.userService=userService;
        this.votingService=votingService;
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
        answer.setUser(userService.getLoggedInUser());
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

    @Override
    public void upvoteAnswer(Long id) {
        Answer answer= answerRepository.findById(id).get();

        Long reputaionPoints=answer.getUser().getReputation()+ ActionPoints.UPVOTE_ANSWER.getPoints();
        answer.getUser().setReputation(reputaionPoints);

        votingService.saveVoteForAnswer(true,false,answer);
        answerRepository.save(answer);
    }

    @Override
    public void downvoteAnswer(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user=userService.getUserByEmail(authentication.getName());
        Answer answer=answerRepository.findById(id).get();

        Long reputationPointsForAuthor=answer.getUser().getReputation()- ActionPoints.DOWNVOTE_AUTHOR.getPoints();
        Long reputationPointsForUser=user.getReputation()-ActionPoints.DOWNVOTE_USER.getPoints();

        answer.getUser().setReputation(reputationPointsForAuthor);
        user.setReputation(reputationPointsForUser);

        userService.saveUser(user);
        votingService.saveVoteForAnswer(false,true,answer);
        answerRepository.save(answer);
    }
}