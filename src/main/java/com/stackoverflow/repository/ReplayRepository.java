package com.stackoverflow.repository;

import com.stackoverflow.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplayRepository extends JpaRepository<Reply,Long> {
}
