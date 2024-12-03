package com.stackoverflow.service;

import com.stackoverflow.model.*;
import com.stackoverflow.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    //private PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(String username, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered.");
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setProfile(new Profile());
        // Hash the password using BCrypt
        user.setPassword(passwordEncoder.encode(password));

        // Assign default role
        user.getRoles().add("ROLE_USER");
        return userRepository.save(user);
    }


    @Override
    public User authenticateUser(String email, String password) {
        return null;
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found."));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User with username " + username + " not found."));
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).get();
    }

    @Override
    public User updateUserProfile(Long userId, String bio) {
        User user = getUserById(userId);
        Profile profile = user.getProfile();
        profile.setBio(bio);
        user.setProfile(profile);
        return userRepository.save(user);
    }

    @Override
    public List<Question> getQuestionsByUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent())
            return optionalUser.get().getQuestions();
        throw new RuntimeException("User not found");
    }

    @Override
    public List<Answer> getAnswersByUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent())
            return optionalUser.get().getAnswers();
        throw new RuntimeException("User not found to load answers");
    }

    @Override
    public List<Notification> getNotificationsByUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent())
            return optionalUser.get().getNotifications();
        throw new RuntimeException("User not found to load notifications");
    }

    public List<Comment> getCommentsByUser(Long userId){
        Optional<User> user = userRepository.findById(userId);
        return user.get().getComments();
    }

    @Override
    public void deleteUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User login(String email, String password) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user found with the provided email."));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password.");
        }

        return user;
    }

//    public User login(String email, String password) {
//        if (email == null || email.isEmpty()) {
//            throw new IllegalArgumentException("Email is required.");
//        }
//        if (password == null || password.isEmpty()) {
//            throw new IllegalArgumentException("Password is required.");
//        }
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new IllegalArgumentException("No user found with the provided email."));
//
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new IllegalArgumentException("Invalid password.");
//        }
//
//        return user;
//    }
}
