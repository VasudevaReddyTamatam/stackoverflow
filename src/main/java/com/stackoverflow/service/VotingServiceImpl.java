package com.stackoverflow.service;

import com.stackoverflow.model.Answer;
import com.stackoverflow.model.Question;
import com.stackoverflow.model.User;
import com.stackoverflow.model.Voting;
import com.stackoverflow.repository.QuestionRepository;
import com.stackoverflow.repository.VotingRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VotingServiceImpl implements VotingService{

    private final VotingRepository votingRepository;
    private final UserService userService;

    VotingServiceImpl(VotingRepository votingRepository,UserService userService){
        this.votingRepository=votingRepository;
        this.userService=userService;
    }

    @Override
    public Question saveVoteForQuestion(boolean upvote, boolean downvote, Question question) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        Optional<Voting> voting = votingRepository.findByQuestionAndUser(question,user);
        if (voting.isPresent()) {
            Voting vote = voting.get();


            if (vote.isUpvote() && downvote) {

                vote.setUpvote(false);
                vote.setDownvote(true);
                question.setUpvotes(question.getUpvotes() - 1);
                question.setDownvotes(question.getDownvotes() + 1);
            } else if (vote.isDownvote() && upvote) {

                vote.setDownvote(false);
                vote.setUpvote(true);
                question.setDownvotes(question.getDownvotes() - 1);
                question.setUpvotes(question.getUpvotes() + 1);
            } else if (!vote.isUpvote() && !vote.isDownvote()) {

                if (upvote) {
                    vote.setUpvote(true);
                    question.setUpvotes(question.getUpvotes() + 1);
                } else if (downvote) {
                    vote.setDownvote(true);
                    question.setDownvotes(question.getDownvotes() + 1);
                }
            } else {

                if (vote.isUpvote()) question.setUpvotes(question.getUpvotes() - 1);
                if (vote.isDownvote()) question.setDownvotes(question.getDownvotes() - 1);
                vote.setUpvote(false);
                vote.setDownvote(false);
            }
            votingRepository.save(vote);
        } else {

            Voting newVote = new Voting();
            newVote.setQuestion(question);
            newVote.setUpvote(upvote);
            newVote.setDownvote(downvote);
            newVote.setUser(user);

            if (upvote){
                question.setUpvotes(question.getUpvotes() + 1);
            }
            if (downvote){
                question.setDownvotes(question.getDownvotes() + 1);
            }

            votingRepository.save(newVote);
        }
        return question;
    }

    @Override
    public Answer saveVoteForAnswer(boolean upvote, boolean downvote, Answer answer) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        Optional<Voting> voting = votingRepository.findByAnswerAndUser(answer,user);
        if (voting.isPresent()) {
            Voting vote = voting.get();

            if (vote.isUpvote() && downvote) {
                vote.setUpvote(false);
                vote.setDownvote(true);
                answer.setUpvotes(answer.getUpvotes() - 1);
                answer.setDownvotes(answer.getDownvotes() + 1);
            } else if (vote.isDownvote() && upvote) {
                vote.setDownvote(false);
                vote.setUpvote(true);
                answer.setDownvotes(answer.getDownvotes() - 1);
                answer.setUpvotes(answer.getUpvotes() + 1);
            } else if (!vote.isUpvote() && !vote.isDownvote()) {

                if (upvote) {
                    vote.setUpvote(true);
                    answer.setUpvotes(answer.getUpvotes() + 1);
                } else if (downvote) {
                    vote.setDownvote(true);
                    answer.setDownvotes(answer.getDownvotes() + 1);
                }
            } else {

                if (vote.isUpvote()){
                    answer.setUpvotes(answer.getUpvotes() - 1);
                }
                if (vote.isDownvote()){
                    answer.setDownvotes(answer.getDownvotes() - 1);
                }
                vote.setUpvote(false);
                vote.setDownvote(false);
            }
            votingRepository.save(vote);
        } else {

            Voting newVote = new Voting();
            newVote.setAnswer(answer);
            newVote.setUpvote(upvote);
            newVote.setDownvote(downvote);
            newVote.setUser(user);

            if (upvote){
                answer.setUpvotes(answer.getUpvotes() + 1);
            }
            if (downvote){
                answer.setDownvotes(answer.getDownvotes() + 1);
            }

            votingRepository.save(newVote);
        }
        return answer;
    }

}
