package org.capps.service.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.capps.entity.User;
import org.capps.repository.UserRepository;
import org.capps.repository.implementation.UserRepositoryImpl;
import org.capps.service.UserService;
import org.capps.servlet.PasswordUtils;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class UserServiceImpl implements UserService {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private EntityManager em;
    private UserRepository userRepository;

    public UserServiceImpl() {
        em = emf.createEntityManager();
        userRepository = new UserRepositoryImpl(em);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.getAllUsers();
        if (users == null){
            throw new RuntimeException("Error fetching users");
        }
        return users;
    }

    @Override
    public void addUser(User user) {
        if (user == null || user.equals(new User())){
            throw new IllegalArgumentException("User cannot be null.");
        }
        userRepository.addUser(user);
    }

    @Override
    public void updateUser(User user) {
        if (user == null || user.equals(new User())) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        userRepository.updateUser(user);
    }

    @Override
    public void deleteUser(int id) {
        User user = userRepository.getUserById(id);
        if (user == null){
            throw new IllegalArgumentException("User cannot be null.");
        }
        userRepository.deleteUser(id);
    }

    @Override
    public User getUserById(int id) {
        User user = userRepository.getUserById(id);
        return user;
    }

    @Override
    public User validateLogin(String email, String password){
        User user = userRepository.findByEmail(email);

        if (user != null){
            try {
                String hashPassword = PasswordUtils.hashPassword(password);
                if(hashPassword.equals(user.getPassword())){
                    return user;
                }
            }catch (NoSuchAlgorithmException e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
