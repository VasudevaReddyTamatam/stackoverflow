package com.stackoverflow.controller;

import com.stackoverflow.model.Question;
import com.stackoverflow.model.User;
import com.stackoverflow.service.QuestionService;
import com.stackoverflow.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DashboardController {
    private final QuestionService questionService;
    private final UserService userService;

    DashboardController(QuestionService questionService,UserService userService){
        this.questionService=questionService;
        this.userService=userService;
    }
    @GetMapping("/home")
    public String homePage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("user", null);
        }
        else {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);
            model.addAttribute("user", user);
        }
        List<Question> questionList=questionService.getAllQuestions();
        model.addAttribute("questions",questionList);
        return "dashboard";
    }

    @GetMapping("/search")
    public String searchByText(@RequestParam("text") String text,Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("user", null);
        }
        else {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);
            model.addAttribute("user", user);
        }
        List<Question> questionList=questionService.searchQuestionByText(text);
        model.addAttribute("questions",questionList);
        return "dashboard";
    }
}
