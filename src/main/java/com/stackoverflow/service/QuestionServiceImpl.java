package com.stackoverflow.service;

import com.stackoverflow.constants.ActionPoints;
import com.stackoverflow.dto.QuestionRequestDTO;
import com.stackoverflow.model.Answer;
import com.stackoverflow.model.Question;
import com.stackoverflow.model.Tag;
import com.stackoverflow.model.User;
import com.stackoverflow.repository.AnswerRepository;
import com.stackoverflow.repository.QuestionRepository;
import com.stackoverflow.repository.TagRepository;
import com.stackoverflow.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService{

    private final QuestionRepository questionRepository;
    private final TagRepository tagRepository;
    private final AnswerRepository answerRepository;
    private final ModelMapper modelMapper;
    private final UserServiceImpl userService;
    private final AnswerService answerService;
    private final VotingService votingService;

    public QuestionServiceImpl(QuestionRepository questionRepository, TagRepository tagRepository, AnswerRepository answerRepository,
                               ModelMapper modelMapper, UserServiceImpl userService, AnswerService answerService
                               ,VotingService votingService) {
        this.questionRepository = questionRepository;
        this.tagRepository = tagRepository;
        this.answerRepository = answerRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.answerService = answerService;
        this.votingService=votingService;
    }

    @Override
    @Transactional
    public Question createQuestion(QuestionRequestDTO questionRequestDTO) {
        Question question = modelMapper.map(questionRequestDTO, Question.class);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        question.setUser(user);
        Set<Tag> tags = questionRequestDTO.getTagsList().stream()
                .map(tagName -> {
                    Tag tag = tagRepository.findByName(tagName);
                    return (tag != null) ? tag : new Tag(tagName);
                })
                .collect(Collectors.toSet());
        question.setTags(tags);
        if(question.getBounties()!=null){
            question.getUser().setReputation(user.getReputation()-question.getBounties());
        }
        Question createdQuestion = questionRepository.save(question);
        return createdQuestion;
    }

    @Override
    public Question getQuestionById(Long id) {
        Optional<Question> question=questionRepository.findById(id);
        return question.get();
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    @Override
    public void upvoteQuestion(Long id) {
        Question question= questionRepository.findById(id).get();
        votingService.saveVoteForQuestion(true,false,question);

        Long reputaionPoints=question.getUser().getReputation()+ ActionPoints.UPVOTE_QUESTION.getPoints();
        question.getUser().setReputation(reputaionPoints);

        questionRepository.save(question);
    }

    @Override
    public void downvoteQuestion(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user=userService.getUserByEmail(authentication.getName());
        Question question= questionRepository.findById(id).get();

        Long reputationPointsForAuthor=question.getUser().getReputation()-ActionPoints.DOWNVOTE_AUTHOR.getPoints();
        Long reputationPointsForUser=user.getReputation()-ActionPoints.DOWNVOTE_USER.getPoints();

        user.setReputation(reputationPointsForUser);
        question.getUser().setReputation(reputationPointsForAuthor);

        userService.saveUser(user);
        questionRepository.save(question);

        question=votingService.saveVoteForQuestion(false,true,question);
        questionRepository.save(question);
    }

    @Override
    @Transactional
    public void updateQuestionWithDTO(Long id, QuestionRequestDTO questionRequestDTO) {
        Question existingQuestion = questionRepository.findById(id).get();

        existingQuestion.setTitle(questionRequestDTO.getTitle());
        existingQuestion.setDescription(questionRequestDTO.getDescription());
        Set<Tag> tags = questionRequestDTO.getTagsList().stream()
                .map(tagName -> {
                    Tag tag = tagRepository.findByName(tagName);
                    return (tag != null) ? tag : new Tag(tagName);
                })
                .collect(Collectors.toSet());
        existingQuestion.setTags(tags);
        existingQuestion.setUpdatedAt(LocalDateTime.now());
        questionRepository.save(existingQuestion);
    }

    @Override
    public void updateQuestion(Long id, Question question){
        question.setUpdatedAt(LocalDateTime.now());
        question.setCreatedAt(LocalDateTime.now());
        questionRepository.save(question);
    }

    @Override
    @Transactional
    public void acceptAnswer(Long questionId, Long answerId) {
        Question question = questionRepository.findById(questionId).get();
        Answer answer = answerRepository.findById(answerId).get();

        question.setAcceptedAnswer(answer);
        question.setStatus("closed");
        answer.setIsAccepted(true);

        Long questionReputationPoints=question.getUser().getReputation()+ActionPoints.QUESTION_ASKER.getPoints();
        Long answerReputaionPoints=answer.getUser().getReputation()+ActionPoints.ACCEPTED_ANSWER.getPoints();

        question.getUser().setReputation(questionReputationPoints);
        answer.getUser().setReputation(answerReputaionPoints);

        if(question.getBounties()!=null){
            Long bounties=question.getBounties()+answer.getUser().getReputation();
            answer.getUser().setReputation(bounties);
        }

        questionRepository.save(question);
        answerRepository.save(answer);

    }

    public List<Question> getAllQuestionsSortedByLatest() {
        return questionRepository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt"));
    }

    public List<Question> getAllQuestionsSortedByOldest() {
        return questionRepository.findAll(Sort.by(Sort.Direction.ASC, "updatedAt"));
    }
}
