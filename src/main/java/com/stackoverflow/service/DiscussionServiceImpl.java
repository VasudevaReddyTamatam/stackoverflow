package com.stackoverflow.service;

import com.stackoverflow.dto.QuestionRequestDTO;
import com.stackoverflow.model.Discussion;
import com.stackoverflow.model.Question;
import com.stackoverflow.model.Tag;
import com.stackoverflow.model.User;
import com.stackoverflow.repository.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DiscussionServiceImpl implements DiscussionService{

    private final DiscussionRepository discussionRepository;
    private final TagRepository tagRepository;
    private final AnswerRepository answerRepository;
    private final ModelMapper modelMapper;
    private final UserServiceImpl userService;
    private final UserRepository userRepository;
    private final AnswerService answerService;

    public DiscussionServiceImpl(DiscussionRepository discussionRepository, TagRepository tagRepository, AnswerRepository answerRepository,
                               ModelMapper modelMapper, UserServiceImpl userService, UserRepository userRepository, AnswerService answerService) {
        this.discussionRepository = discussionRepository;
        this.tagRepository = tagRepository;
        this.answerRepository = answerRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.userRepository = userRepository;
        this.answerService = answerService;
    }

    @Override
    @Transactional
    public Discussion createDiscussion(QuestionRequestDTO questionRequestDTO) {
        Discussion discussion = modelMapper.map(questionRequestDTO, Discussion.class);
        User user=userService.getUserById(1L);
        discussion.setUser(user);

        Set<Tag> tags = questionRequestDTO.getTagsList().stream()
                .map(tagName -> {
                    Tag tag = tagRepository.findByName(tagName);
                    return (tag != null) ? tag : new Tag(tagName);
                })
                .collect(Collectors.toSet());
        discussion.setDiscussionTags(tags);

        Discussion createdDiscussion = discussionRepository.save(discussion);
        return createdDiscussion;
    }

    @Override
    public Discussion getDiscussionById(Long id) {
        return discussionRepository.findById(id).get();
    }

    @Override
    public List<Discussion> getAllDiscussion() {
        return discussionRepository.findAll();
    }
}
