package com.stackoverflow.service;

import com.stackoverflow.model.Comment;
import com.stackoverflow.model.Tag;

import java.util.List;
import java.util.Set;

public interface TagService {
    public List<Tag> findAllTags();
    public void saveTag(Tag tag);
    Tag findTagById(long id);
    public void deleteTagById(long id);
    List<Tag> searchTagsByName(String keyword);
}
