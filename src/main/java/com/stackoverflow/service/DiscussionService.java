package com.stackoverflow.service;

import com.stackoverflow.dto.QuestionRequestDTO;
import com.stackoverflow.model.Discussion;
import com.stackoverflow.model.Question;

import java.util.List;

public interface DiscussionService {
    Discussion createDiscussion(QuestionRequestDTO questionRequestDTO);
    Discussion getDiscussionById(Long id);
    List<Discussion> getAllDiscussion();
}
