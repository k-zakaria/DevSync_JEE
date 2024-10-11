package org.capps.repository.implementation;

import org.capps.entity.Tag;
import org.capps.entity.Task;
import jakarta.persistence.EntityManager;
import org.capps.repository.TagRepository;

import java.util.List;

public class TagRepositoryImpl implements TagRepository {

    private EntityManager em;

    public TagRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Tag> getAllTags() {
        return em.createQuery("SELECT t FROM Tag t", Tag.class).getResultList();
    }

    @Override
    public void addTag(Tag tag) {
        em.getTransaction().begin();
        em.persist(tag);
        em.getTransaction().commit();
    }

    @Override
    public void updateTag(Tag tag) {
        em.getTransaction().begin();
        em.merge(tag);
        em.getTransaction().commit();
    }

    @Override
    public Tag getTagById(int id) {
        return em.find(Tag.class, id);
    }

    @Override
    public void deleteTag(int id) {
        em.getTransaction().begin();
        Tag tag = em.find(Tag.class, id);
        if (tag != null) {
            em.remove(tag);
        }
        em.getTransaction().commit();
    }
}
