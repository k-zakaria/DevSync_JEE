package org.capps.service.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.capps.entity.User;
import org.capps.entity.UserRole;
import org.capps.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private EntityManager em;

    public UserServiceImpl() {
        em = emf.createEntityManager();
    }

    @Override
    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public void addUser(User user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }

    @Override
    public void addUser(String username, String password, String firstName, String lastName, String email, UserRole role) {
        // Validation des inputs
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom d'utilisateur ne peut pas être vide.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide.");
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide.");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de famille ne peut pas être vide.");
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("L'email est invalide.");
        }
        if (role == null) {
            throw new IllegalArgumentException("Le rôle ne peut pas être vide.");
        }

        User user = new User(username, password, firstName, lastName, email, role);
        addUser(user);
    }

    @Override
    public void updateUser(User user) {
        // Validation de l'utilisateur
        if (user == null) {
            throw new IllegalArgumentException("L'utilisateur ne peut pas être null.");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom d'utilisateur ne peut pas être vide.");
        }
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide.");
        }
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de famille ne peut pas être vide.");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty() || !user.getEmail().contains("@")) {
            throw new IllegalArgumentException("L'email est invalide.");
        }
        if (user.getRole() == null) {
            throw new IllegalArgumentException("Le rôle ne peut pas être vide.");
        }
        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();
    }

    @Override
    public void deleteUser(int id) {
        em.getTransaction().begin();
        User user = em.find(User.class, id);
        if (user != null) {
            em.remove(user);
        }
        em.getTransaction().commit();
    }

    @Override
    public User getUserById(int id) {
        return em.find(User.class, id);
    }
}
