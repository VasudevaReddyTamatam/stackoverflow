package com.stackoverflow.service;

import com.stackoverflow.model.Answer;
import com.stackoverflow.model.Question;

public interface VotingService {
    Question saveVoteForQuestion(boolean upvote, boolean downvote, Question question);
    Answer saveVoteForAnswer(boolean upvote, boolean downvote, Answer answer);
}
