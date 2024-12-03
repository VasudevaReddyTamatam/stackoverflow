package com.stackoverflow.controller;

import com.stackoverflow.model.Comment;
import com.stackoverflow.model.Question;
import com.stackoverflow.service.AnswerService;
import com.stackoverflow.service.CommentService;
import com.stackoverflow.service.QuestionService;
import com.stackoverflow.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("comment")
public class CommentController {
    private final CommentService commentService;
    private final QuestionService questionService;
    private final UserService userService;
    private final AnswerService answerService;

    CommentController(CommentService commentService,QuestionService questionService,UserService userService,AnswerService answerService){
        this.commentService=commentService;
        this.questionService=questionService;
        this.userService=userService;
        this.answerService=answerService;
    }


    @PostMapping("answer/{id}")
    public String createanswerComment(@PathVariable("id") Long answerId, @RequestParam("comment") String comment, Model model){
        Comment c=new Comment();
        c.setContent(comment);
        c.setUser(userService.getUserById(1L));
        c.setAnswer(answerService.findAnswerById(answerId));
        return "question/detail";
    }

}
