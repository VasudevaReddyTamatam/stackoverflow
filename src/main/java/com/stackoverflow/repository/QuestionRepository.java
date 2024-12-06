package com.stackoverflow.repository;

import com.stackoverflow.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Long> {
    Page<Question> findAllByOrderByUpdatedAtDesc(Pageable pageable);

    Page<Question> findAllByOrderByUpdatedAtAsc(Pageable pageable);
}
