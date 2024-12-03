package com.stackoverflow.service;

import com.stackoverflow.model.Tag;
import com.stackoverflow.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagDao;

    public TagServiceImpl(TagRepository tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<Tag> findAllTags() {
        return tagDao.findAll();
    }

    @Override
    public void saveTag(Tag tag) {
        tagDao.save(tag);
    }

    @Override
    public Tag findTagById(long id) {
        return tagDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Tag not found"));
    }

    @Override
    public void deleteTagById(long id) {
        if (!tagDao.existsById(id)) {
            throw new IllegalArgumentException("Tag with ID " + id + " does not exist");
        }
        tagDao.deleteById(id);
    }
    @Override
    public List<Tag> searchTagsByName(String keyword) {
        return tagDao.findByNameContainingIgnoreCase(keyword); // Case-insensitive search
    }
}