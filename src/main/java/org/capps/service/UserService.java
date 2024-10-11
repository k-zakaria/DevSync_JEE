package org.capps.service;

import org.capps.entity.User;
import org.capps.entity.UserRole;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(int id);
    User getUserById(int id);
}
