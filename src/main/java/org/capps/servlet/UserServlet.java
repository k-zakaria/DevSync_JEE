package org.capps.servlet;

import jakarta.servlet.annotation.WebServlet;
import org.capps.entity.User;
import org.capps.entity.UserRole;
import org.capps.service.UserService;
import org.capps.service.implementation.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private UserService userService; // Utiliser l'interface ici

    public UserServlet() {
        try {
            this.userService = new UserServiceImpl();
            System.out.println("UserService initialized successfully");
        } catch (Exception e) {
            System.out.println("Error during UserService initialization: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize UserServlet", e);
        }
    }


    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("UserServlet init method called");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doGet called: fetching all users");

        List<User> users = userService.getAllUsers();
        if (users == null || users.isEmpty()) {
            System.out.println("No users found in the database.");
        } else {
            System.out.println("Users retrieved: " + users.size() + " users found.");
        }

        request.setAttribute("users", users);
        request.getRequestDispatcher("/views/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPost called");

        String method = request.getParameter("_method");
        if (method != null) {
            System.out.println("Method parameter: " + method);
        }

        if ("DELETE".equalsIgnoreCase(method)) {
            doDelete(request, response);
        } else if ("PUT".equalsIgnoreCase(method)) {
            doPut(request, response);
        } else {
            System.out.println("Adding new user...");
            User user = createUserFromRequest(request, response);
            if (user == null) {
                System.out.println("User creation failed. Exiting doPost.");
                return;
            }

            try {
                userService.addUser(user);
                System.out.println("User added successfully");
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.sendRedirect(request.getContextPath() + "/users");
            } catch (Exception e) {
                System.out.println("Error adding user: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to add user.");
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPut called: updating user");

        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
            System.out.println("Updating user with ID: " + id);
        } catch (NumberFormatException e) {
            System.out.println("Invalid user ID provided for update.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
            return;
        }

        User user = createUserFromRequest(request, response);
        if (user == null) {
            System.out.println("User creation from request failed in doPut.");
            return;
        }

        user.setId(id);

        try {
            userService.updateUser(user);
            System.out.println("User with ID " + id + " updated successfully.");
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect(request.getContextPath() + "/users");
        } catch (Exception e) {
            System.out.println("Error updating user: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while updating the user.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doDelete called: deleting user");

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            System.out.println("Deleting user with ID: " + id);
            userService.deleteUser(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            response.sendRedirect(request.getContextPath() + "/users");
        } catch (NumberFormatException e) {
            System.out.println("Invalid user ID provided for delete.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while deleting the user");
        }
    }

    private User createUserFromRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Creating user from request parameters");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String roleParam = request.getParameter("role");

        // Validate parameters
        if (username == null || password == null || firstName == null || lastName == null || email == null || roleParam == null) {
            System.out.println("Missing required parameters for user creation");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "All fields are required.");
            return null;
        }

        UserRole role;
        try {
            role = UserRole.valueOf(roleParam.toUpperCase());
            System.out.println("Role provided: " + role);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid role: " + roleParam);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user role.");
            return null;
        }

        System.out.println("User created successfully from request");
        return new User(username, password, firstName, lastName, email, role);
    }
}
