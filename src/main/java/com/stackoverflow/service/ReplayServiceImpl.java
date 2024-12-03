package com.stackoverflow.service;

import com.stackoverflow.model.Reply;
import com.stackoverflow.repository.ReplayRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplayServiceImpl implements ReplayService{
    ReplayRepository replayRepository;

    ReplayServiceImpl(ReplayRepository replayRepository){
        this.replayRepository=replayRepository;
    }

    @Override
    public Reply saveReplay(Reply replay) {
        return replayRepository.save(replay);
    }

    @Override
    public Reply getReplayById(Long id) {
        return replayRepository.findById(id).get();
    }

    @Override
    public List<Reply> getAllReplay() {
        return replayRepository.findAll();
    }
}
