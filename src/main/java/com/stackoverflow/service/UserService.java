package com.stackoverflow.service;

import com.stackoverflow.model.*;

import java.util.List;

public interface UserService {
    User registerUser(String username, String email, String password);
    User authenticateUser(String email, String password);
    User getUserById(Long userId);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    User updateUserProfile(Long userId, String bio);
    List<Question> getQuestionsByUser(Long userId);
    List<Answer> getAnswersByUser(Long userId);
    List<Notification> getNotificationsByUser(Long userId);
    List<Comment> getCommentsByUser(Long userId);
    void deleteUser(Long userId);
    void saveUser(User user);
    User login(String email, String password);
}
