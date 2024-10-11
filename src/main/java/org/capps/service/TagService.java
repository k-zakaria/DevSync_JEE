package org.capps.service;

import org.capps.entity.Tag;
import java.util.List;

public interface TagService {
    List<Tag> getAllTags();
    void addTag(Tag tag);
    void updateTag(Tag Tag);
    Tag getTagById(int id);
    void deleteTag(int id);
}
