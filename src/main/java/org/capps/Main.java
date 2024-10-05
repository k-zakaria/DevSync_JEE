package org.capps;

import org.capps.entity.User;
import org.capps.entity.UserRole;
import org.capps.service.UserService;

public class Main {
    public static void main(String[] args) {

//                UserService userService = new UserService();
//
//                User user1 = new User("John Doe", "password123", "John", "Doe", "john.doe@example.com", UserRole.USER);
//                userService.addUser(user1);
//                System.out.println("User added successfully!");
//
//                System.out.println("\nListe des utilisateurs :");
//                userService.getAllUsers().forEach(user ->
//                        System.out.println("ID: " + user.getId() + ", Name: " + user.getUsername() + ", Email: " + user.getEmail() + ", Role: " + user.getRole())
//                );


        UserService userService = new UserService();

// Step 1: Add a new user
        User user1 = new User("John Doe", "password123", "John", "Doe", "john.doe@example.com", UserRole.USER);
        userService.addUser(user1);
        System.out.println("User added successfully!");

// Step 2: Update the user's information
        user1.setUsername("JohnUpdated");
        user1.setEmail("john.updated@example.com");

// Call the update method
        userService.updateUser(user1);
        System.out.println("User updated successfully!");

// Step 3: Fetch all users and display updated info
        System.out.println("\nListe des utilisateurs après l'update :");
        userService.getAllUsers().forEach(user ->
                System.out.println("ID: " + user.getId() + ", Name: " + user.getUsername() + ", Email: " + user.getEmail() + ", Role: " + user.getRole())
        );
            }



}
