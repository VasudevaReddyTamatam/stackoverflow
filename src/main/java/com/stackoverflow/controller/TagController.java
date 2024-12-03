package com.stackoverflow.controller;

import com.stackoverflow.model.Question;
import com.stackoverflow.model.Tag;
import com.stackoverflow.service.TagService;
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

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public String listTags(Model model) {
        List<Tag> tags = tagService.findAllTags();
        model.addAttribute("tags", tags);
        return "tags"; // Renders tags.html
    }

    @GetMapping("/search")
    public String searchTags(@RequestParam("keyword") String keyword, Model model) {
        List<Tag> tags = tagService.searchTagsByName(keyword);
        model.addAttribute("tags", tags);
        model.addAttribute("keyword", keyword);
        return "tags";
    }

    @GetMapping("/{tagId}/questions")
    public String listQuestionsByTag(@PathVariable Long tagId, Model model) {
        Tag selectedTag = tagService.findTagById(tagId);
        Set<Question> questions = selectedTag.getQuestion();
        model.addAttribute("questions", questions);
        model.addAttribute("selectedTagName", selectedTag.getName());
        return "tags";
    }
}