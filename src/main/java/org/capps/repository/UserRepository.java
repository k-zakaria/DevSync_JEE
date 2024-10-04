package org.capps.repository;

import org.capps.entity.User;

import jakarta.persistence.EntityManager;
import java.util.List;

public class UserRepository {

    private EntityManager em;

    public UserRepository(EntityManager em) {
        this.em = em;
    }

    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public void addUser(User user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }

    public void updateUser(User user) {
        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();
    }

    public User getUserById(int id) {
        return em.find(User.class, id);
    }

    public void deleteUser(int id) {
        em.getTransaction().begin();
        User user = em.find(User.class, id);
        if (user != null) {
            em.remove(user);
        }
        em.getTransaction().commit();
    }
}
