package com.stackoverflow.service;

import com.stackoverflow.model.Comment;

import java.util.List;

public interface CommentService {
    public List<Comment> findAllComments();
    public void saveComment(Comment comment);
    public void deleteCommentById(long id);
}
