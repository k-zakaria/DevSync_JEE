package org.capps;

import org.capps.entity.User;
import org.capps.entity.UserRole;
import org.capps.service.UserService;

public class Main {
    public static void main(String[] args) {

                UserService userService = new UserService();

                User user1 = new User("John Doe", "password123", "John", "Doe", "john.doe@example.com", UserRole.USER);
                userService.addUser(user1);
                System.out.println("User added successfully!");

                System.out.println("\nListe des utilisateurs :");
                userService.getAllUsers().forEach(user ->
                        System.out.println("ID: " + user.getId() + ", Name: " + user.getUsername() + ", Email: " + user.getEmail() + ", Role: " + user.getRole())
                );
            }



}
