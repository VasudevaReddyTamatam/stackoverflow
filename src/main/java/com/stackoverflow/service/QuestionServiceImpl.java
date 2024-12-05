package com.stackoverflow.service;

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
    private final UserRepository userRepository;
    private final AnswerService answerService;

    public QuestionServiceImpl(QuestionRepository questionRepository, TagRepository tagRepository, AnswerRepository answerRepository,
                               ModelMapper modelMapper, UserServiceImpl userService, UserRepository userRepository, AnswerService answerService) {
        this.questionRepository = questionRepository;
        this.tagRepository = tagRepository;
        this.answerRepository = answerRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.userRepository = userRepository;
        this.answerService = answerService;
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

    @Transactional
    @Override
    public List<Question> searchQuestionByText(String text) {
//        List<Question> allQuestions = questionRepository.findAll();
//        return allQuestions.stream()
//                .filter(q -> q.getDescription().replaceAll("<[^>]+>", "")
//                        .toLowerCase().contains(text.toLowerCase()))
//                .collect(Collectors.toList());
        List<Question> t= questionRepository.getPlainTextFromLob();
        for(Question q:t) {
            System.out.println(q.getDescription() + "hiiemnwdke.............................................................");

        }
        return questionRepository.searchByContent(text);
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


}
