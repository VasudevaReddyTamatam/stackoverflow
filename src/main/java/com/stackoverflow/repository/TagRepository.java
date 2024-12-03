package com.stackoverflow.repository;

import com.stackoverflow.model.Tag;
import jakarta.persistence.SecondaryTable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag,Long> {
    Set<Tag> findByNameContainingIgnoreCase(String keyword);
    Tag findByName(String tag);
}
