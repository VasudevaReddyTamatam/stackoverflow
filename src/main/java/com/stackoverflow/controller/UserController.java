package com.stackoverflow.controller;

import com.stackoverflow.dto.UserLoginRequest;
import com.stackoverflow.model.*;
import com.stackoverflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("user", null);
            return "/dashboard";
        }
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        return "/dashboard";
    }


    @GetMapping("/register")
    @PreAuthorize("permitAll()")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "user/registerationform";
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        System.out.println("Username : " + user.getUsername() + "\t email : " + user.getEmail() + "\t password : " + user.getPassword());
        userService.registerUser(user.getUsername(),user.getEmail(),user.getPassword());
        return "redirect:/users/login";
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new UserLoginRequest());
        return "user/login";
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public String loginUser(@ModelAttribute("loginRequest") UserLoginRequest request, Model model) {
        try {
            User user = userService.login(request.getEmail(), request.getPassword());
            model.addAttribute("user", user);
            return "forward:/home";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "user/login";
        }
    }

   @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
   @GetMapping("/profile/{userId}")
    public String getUserProfile(@PathVariable Long userId, Model model) {

       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       System.out.println("Authentication: " + authentication);
       System.out.println("Authorities: " + authentication.getAuthorities());
       System.out.println("Principal: " + authentication.getPrincipal());

       User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "user/profile";
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN') ")
    @GetMapping("/{userId}/profile/edit")
    public String showUpdateProfileForm(@PathVariable Long userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "user/editprofile";
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN') ")
    @PatchMapping("/{userId}/profile/update")
    public String updateUserProfile(@PathVariable Long userId, @ModelAttribute("user") User user) {
        userService.updateUserProfile(userId, user.getProfile().getBio());
        return "redirect:/users/profile/" + userId;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN') ")
    @GetMapping("/{userId}/questions")
    public String getUserQuestions(@PathVariable Long userId, Model model) {
        List<Question> questions = userService.getQuestionsByUser(userId);
        User user = userService.getUserById(userId);
        model.addAttribute("questions", questions);
        model.addAttribute("user",user);
        return "user/questions";
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN') ")
    @GetMapping("/{userId}/answers")
    public String getUserAnswers(@PathVariable Long userId, Model model) {
        List<Answer> answers = userService.getAnswersByUser(userId);
        User user = userService.getUserById(userId);
        model.addAttribute("user",user);
        model.addAttribute("answers", answers);
        return "user/answers";
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN') ")
    @GetMapping("/{userId}/notifications")
    public String getUserNotifications(@PathVariable Long userId, Model model) {
        List<Notification> notifications = userService.getNotificationsByUser(userId);
        model.addAttribute("notifications", notifications);
        return "user/notifications";
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN') ")
    @GetMapping("/{userId}/comments")
    public String getCommentsByUser(@PathVariable Long userId, Model model){
        List<Comment> comments = userService.getCommentsByUser(userId);
        User user = userService.getUserById(userId);
        model.addAttribute("comments",comments);
        model.addAttribute("user",user);
        return "user/comments";
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN') ")
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
    }
}
