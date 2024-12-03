package com.stackoverflow.controller;

import com.stackoverflow.dto.QuestionRequestDTO;
import com.stackoverflow.model.Discussion;
import com.stackoverflow.model.Question;
import com.stackoverflow.service.DiscussionService;
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
    DiscussionController(DiscussionService discussionService){
        this.discussionService=discussionService;
    }

    @GetMapping
    public String getDiscussion(Model model){
        List<Discussion> discussions=discussionService.getAllDiscussion();
        model.addAttribute("discussions",discussions);
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
