package com.stackoverflow.repository;

import com.stackoverflow.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Long> {

//    @Query(value = """
//    SELECT q.*
//    FROM questions q
//    WHERE
//        LOWER(q.title) LIKE LOWER(CONCAT('%', :query, '%')) OR
//        LOWER(regexp_replace(q.description, '<[^>]+>', '', 'g')) LIKE LOWER(CONCAT('%', :query, '%'))
//    """,
//            nativeQuery = true)
//    List<Question> searchQuestionByText(@Param("query") String query);
    @Query(value = """
    select
            q1_0.id,
                    q1_0.created_at,
                    regexp_replace(q1_0.description, '<[^>]+>', '', 'g') as description,
                    q1_0.downvotes,
                    q1_0.status,
                    q1_0.title,
                    q1_0.updated_at,
                    q1_0.upvotes,
                    q1_0.user_id
    from
    questions q1_0
    """,nativeQuery = true)
    List<Question> getPlainTextFromLob();



    @Query(value = "SELECT q FROM Question q WHERE " +
            "LOWER(q.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(FUNCTION('regexp_replace', q.description, '<[^>]+>', '', 'g')) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Question> searchByContent(@Param("query") String query);






}
