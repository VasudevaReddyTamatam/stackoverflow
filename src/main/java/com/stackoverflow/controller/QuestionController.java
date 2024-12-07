package com.stackoverflow.controller;

import com.stackoverflow.dto.QuestionRequestDTO;
import com.stackoverflow.dto.UserLoginRequest;
import com.stackoverflow.model.*;
import com.stackoverflow.repository.TagRepository;
import com.stackoverflow.service.CommentService;
import com.stackoverflow.service.QuestionService;
import com.stackoverflow.service.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final UserServiceImpl userService;
    private final CommentService commentService;
    private final ModelMapper modelMapper;
    private final TagRepository tagRepository;

    public QuestionController(QuestionService questionService, UserServiceImpl userService,
                              CommentService commentService,ModelMapper modelMapper, TagRepository tagRepository) {
        this.questionService = questionService;
        this.userService = userService;
        this.commentService = commentService;
        this.modelMapper=modelMapper;
        this.tagRepository = tagRepository;
    }

//    @GetMapping
//    public String questionDashboard(Model model){
//        List<Question> questionList=questionService.getAllQuestions();
//        model.addAttribute("questions",questionList);
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
//            model.addAttribute("user", null);
//        }
//        else {
//            String email = authentication.getName();
//            User user = userService.getUserByEmail(email);
//            model.addAttribute("user", user);
//        }
//
//        return "question/QuestionDashboard";
//    }

    @GetMapping
    public String questionDashboard(
            @RequestParam(defaultValue = "latest") String sortBy,
            Model model) {

        List<Question> questionList;
        if ("oldest".equalsIgnoreCase(sortBy)) {
            questionList = questionService.getAllQuestionsSortedByOldest();
        } else {
            questionList = questionService.getAllQuestionsSortedByLatest();
        }

        model.addAttribute("questions", questionList);
        model.addAttribute("sortBy", sortBy);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("user", null);
        } else {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);
            model.addAttribute("user", user);
        }

        return "question/QuestionDashboard";
    }

    @GetMapping("/ask")
    public String questionPage(Model model){
        User user=userService.getLoggedInUser();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("loginRequest", new UserLoginRequest());
            return "user/login";
        }
        model.addAttribute("questionRequestDTO", new QuestionRequestDTO());
        model.addAttribute("user",user);
        return "question/create";
    }


    @PostMapping("/create")
    public String createQuestion(@ModelAttribute("questionRequestDTO") QuestionRequestDTO questionRequestDTO) {
        Question createdQuestion = questionService.createQuestion(questionRequestDTO);
        return "redirect:/home";
    }

    @GetMapping("/{id}")
    public String getQuestionById(@PathVariable("id") Long questionId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("user", null);
            model.addAttribute("reputation",0);
            model.addAttribute("username","");
        }
        else {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);
            model.addAttribute("user", user);
            model.addAttribute("reputation",user.getReputation());
            model.addAttribute("username",user.getUsername());
        }
        Question question = questionService.getQuestionById(questionId);
        model.addAttribute("question", question);

        return "question/detail";
    }

    @GetMapping("upvote/{id}")
    public String updateUpvote(@PathVariable("id") Long id,Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("loginRequest", new UserLoginRequest());
            return "user/login";
        }
        questionService.upvoteQuestion(id);
        return "redirect:/questions/" + id;
    }

    @GetMapping("downvote/{id}")
    public String updateDownvote(@PathVariable("id") Long id,Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("loginRequest", new UserLoginRequest());
            return "user/login";
        }
        questionService.downvoteQuestion(id);
        return "redirect:/questions/" + id;
    }
    @PostMapping("comment/{id}")
    public String createComment(@PathVariable("id") Long questionId, @RequestParam("comment") String comment, Model model){
        Comment c=new Comment();
        c.setContent(comment);
        c.setUser(userService.getLoggedInUser());
        c.setQuestion(questionService.getQuestionById(questionId));
        commentService.saveComment(c);
        return "redirect:/questions/" + questionId;
    }

    @GetMapping("/edit/{id}")
    public String editQuestionForm(@PathVariable("id") Long id, Model model) {
        Question existingQuestion = questionService.getQuestionById(id);

        QuestionRequestDTO questionRequestDTO = new QuestionRequestDTO();
        questionRequestDTO.setId(existingQuestion.getId());
        questionRequestDTO.setTitle(existingQuestion.getTitle());
        questionRequestDTO.setDescription(existingQuestion.getDescription());
        questionRequestDTO.setTagsList(
                existingQuestion.getTags().stream()
                        .map(Tag::getName) // Directly map each Tag to its name
                        .collect(Collectors.toSet()) // Collect the names into a Set
        );

        model.addAttribute("questionRequestDTO", questionRequestDTO);// Set the form action URL for updating
        return "question/create";
    }

    @PostMapping("/update/{id}")
    public String updateQuestion(@PathVariable("id") Long id,
                                 @ModelAttribute("questionRequestDTO") QuestionRequestDTO updatedQuestionDetails,
                                 Model model) {
        questionService.updateQuestionWithDTO(id, updatedQuestionDetails);
        return "redirect:/questions/" + id;
    }

    @GetMapping("/delete/{id}")
    public String deleteQuestion(@PathVariable("id") Long id) {
        questionService.deleteQuestion(id);
        return "redirect:/home";
    }

    @PostMapping("/{questionId}/accept-answer/{answerId}")
    public String acceptAnswer(
            @PathVariable Long questionId,
            @PathVariable Long answerId) {

        questionService.acceptAnswer(questionId, answerId);
        return "redirect:/questions/" + questionId;
    }

    @GetMapping("/search")
    public String searchQuestions(@RequestParam("keyword") String keyword, Model model) {
        List<Question> questions = questionService.searchQuestionsByTitle(keyword);

        model.addAttribute("questions", questions);
        model.addAttribute("keyword", keyword);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("user", null);
        } else {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);
            model.addAttribute("user", user);
        }
        return "dashboard";
    }
}
