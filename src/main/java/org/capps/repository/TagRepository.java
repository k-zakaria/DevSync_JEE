package org.capps.repository;

import org.capps.entity.Tag;
import org.capps.entity.Task;

import java.util.List;

public interface TagRepository {
    List<Tag> getAllTags();
    void addTag(Tag tag);
    void updateTag(Tag tag);
    Tag getTagById(int id);
    void deleteTag(int id);
}
