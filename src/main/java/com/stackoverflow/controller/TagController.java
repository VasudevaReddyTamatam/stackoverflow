package com.stackoverflow.controller;

import com.stackoverflow.model.Question;
import com.stackoverflow.model.Tag;
import com.stackoverflow.model.User;
import com.stackoverflow.service.TagService;
import com.stackoverflow.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final UserService userService;

    public TagController(TagService tagService, UserService userService) {
        this.tagService = tagService;
        this.userService = userService;
    }

    @GetMapping
    public String listTags(Model model) {
        List<Tag> tags = tagService.findAllTags();
        model.addAttribute("tags", tags);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("user", null);
        }
        else {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);
            model.addAttribute("user", user);
        }

        return "TagsDashboard";
    }

    @GetMapping("/search")
    public String searchTags(@RequestParam("keyword") String keyword, Model model) {
        Set<Tag> tags = tagService.searchTagsByName(keyword);
        model.addAttribute("tags", tags);
        model.addAttribute("keyword", keyword);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("user", null);
        }
        else {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);
            model.addAttribute("user", user);
        }

        return "TagsDashboard";
    }

    @GetMapping("/{tagId}/questions")
    public String listQuestionsByTag(@PathVariable Long tagId, Model model) {
        Tag selectedTag = tagService.findTagById(tagId);
        Set<Question> questions = selectedTag.getQuestion();
        model.addAttribute("questions", questions);
        model.addAttribute("selectedTagName", selectedTag.getName());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("user", null);
        }
        else {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);
            model.addAttribute("user", user);
        }

        return "TagsDashboard";
    }
}