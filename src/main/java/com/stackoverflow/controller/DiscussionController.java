package com.stackoverflow.controller;

import com.stackoverflow.dto.QuestionRequestDTO;
import com.stackoverflow.model.Discussion;
import com.stackoverflow.model.Question;
import com.stackoverflow.model.User;
import com.stackoverflow.service.DiscussionService;
import com.stackoverflow.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/discussion")
public class DiscussionController {

    private final DiscussionService discussionService;
    private final UserService userService;
    DiscussionController(DiscussionService discussionService, UserService userService){
        this.discussionService=discussionService;
        this.userService = userService;
    }

    @GetMapping
    public String getDiscussion(Model model){
        List<Discussion> discussions=discussionService.getAllDiscussion();
        model.addAttribute("discussions",discussions);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("user", null);
        }
        else {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);
            model.addAttribute("user", user);
        }

        return "discussion/Discussion";
    }

    @GetMapping("/create")
    public  String discussionPage(Model model){
        model.addAttribute("questionRequestDTO", new QuestionRequestDTO());
        return "discussion/CreateDiscussion";
    }

    @PostMapping("/create")
    public String createQuestion(@ModelAttribute("questionRequestDTO") QuestionRequestDTO questionRequestDTO) {
        Discussion createdDiscussion = discussionService.createDiscussion(questionRequestDTO);
        return "redirect:/discussion";
    }
}
