package org.capps.service.implementation;

import org.capps.entity.Tag;
import org.capps.repository.TagRepository;
import org.capps.repository.implementation.TagRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.capps.service.TagService;

import java.util.List;

public class TagServiceImpl implements TagService {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private EntityManager em;
    private TagRepository tagRepository;

    public TagServiceImpl() {
        em = emf.createEntityManager();
        tagRepository = new TagRepositoryImpl(em);
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.getAllTags();
    }

    @Override
    public void addTag(Tag tag) {
        tagRepository.addTag(tag);
    }

    @Override
    public void updateTag(Tag tag) {
        tagRepository.updateTag(tag);
    }

    @Override
    public Tag getTagById(int id) {
        return tagRepository.getTagById(id);
    }

    @Override
    public void deleteTag(int id) {
        tagRepository.deleteTag(id);
    }
}
