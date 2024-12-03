package com.stackoverflow.service;

import com.stackoverflow.repository.CommentRepository;
import com.stackoverflow.model.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentDao;
    CommentServiceImpl(CommentRepository commentDao){
        this.commentDao=commentDao;
    }

    @Override
    public List<Comment> findAllComments() {
        return commentDao.findAll();
    }

    @Override
    public void saveComment(Comment comment) {
        commentDao.save(comment);
    }

    @Override
    public void deleteCommentById(long id) {
        commentDao.deleteById(id);
    }
}
