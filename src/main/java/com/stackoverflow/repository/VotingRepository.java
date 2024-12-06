package com.stackoverflow.repository;

import com.stackoverflow.model.Answer;
import com.stackoverflow.model.Question;
import com.stackoverflow.model.User;
import com.stackoverflow.model.Voting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VotingRepository extends JpaRepository<Voting,Long> {

    Optional<Voting> findByQuestion( Question question);
    Optional<Voting> findByAnswer(Answer answer);
    Optional<Voting> findByQuestionAndUser(Question question, User user);
    Optional<Voting> findByAnswerAndUser(Answer answer, User user);

}
