package com.stackoverflow.repository;

import com.stackoverflow.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long> {
    List<Tag> findByNameContainingIgnoreCase(String keyword);
    Tag findByName(String name);
}
