package org.capps.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.capps.entity.User;
import org.capps.entity.UserRole;

import java.util.List;

public class UserService {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private EntityManager em;

    public UserService() {
        em = emf.createEntityManager();
    }

    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public void addUser(User user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }

    public void addUser(String username, String password, String firstName, String lastName, String email, UserRole role) {
        User user = new User(username, password, firstName, lastName, email, role);
        addUser(user);
    }

    public void updateUser(User user) {
        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();
    }

    public void deleteUser(int id) {
        em.getTransaction().begin();
        User user = em.find(User.class, id);
        if (user != null) {
            em.remove(user);
        }
        em.getTransaction().commit();
    }

    public User getUserById(int id) {
        return em.find(User.class, id);
    }
}
