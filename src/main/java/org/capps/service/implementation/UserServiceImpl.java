package org.capps.service.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.capps.entity.User;
import org.capps.repository.UserRepository;
import org.capps.repository.implementation.UserRepositoryImpl;
import org.capps.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private UserRepository userRepository;

    public UserServiceImpl() {
        EntityManager em = emf.createEntityManager();
        if (em == null) {
            throw new IllegalStateException("EntityManager initialization failed. Check your persistence configuration.");
        }
        this.userRepository = new UserRepositoryImpl(em);
    }

    private EntityManager getEntityManager() {
        EntityManager em = emf.createEntityManager();
        if (em == null) {
            throw new IllegalStateException("EntityManager initialization failed.");
        }
        return em;
    }

    @Override
    public List<User> getAllUsers() {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            List<User> users = userRepository.getAllUsers();
            em.getTransaction().commit();
            return users;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error fetching users", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void addUser(User user) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            userRepository.addUser(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error adding user", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void updateUser(User user) {
        if (user == null || user.equals(new User())) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty.");
        }
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty.");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty() || !user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email.");
        }
        if (user.getRole() == null) {
            throw new IllegalArgumentException("Role cannot be empty.");
        }

        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            userRepository.updateUser(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error updating user", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteUser(int id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            userRepository.deleteUser(id);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error deleting user", e);
        } finally {
            em.close();
        }
    }

    @Override
    public User getUserById(int id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            User user = userRepository.getUserById(id);
            em.getTransaction().commit();
            return user;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error fetching user by ID", e);
        } finally {
            em.close();
        }
    }
}
