package com.stackoverflow.repository;

import com.stackoverflow.model.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscussionRepository extends JpaRepository<Discussion,Long> {
}
