package com.stackoverflow.service;

import com.stackoverflow.model.Reply;

import java.util.List;

public interface ReplayService {
    Reply saveReplay(Reply reply);
    Reply getReplayById(Long id);
    List<Reply> getAllReplay();
}
