package org.capps.repository;

import org.capps.entity.User;
import java.util.List;

public interface UserRepository {
    List<User> getAllUsers();
    void addUser(User user);
    void updateUser(User user);
    User getUserById(int id);
    void deleteUser(int id);
}
