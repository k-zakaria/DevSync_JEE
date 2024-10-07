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

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserServiceImpl(); // Instanciation de l'implémentation
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> users = userService.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/views/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");

        if ("DELETE".equalsIgnoreCase(method)) {
            doDelete(request, response);
        } else if ("PUT".equalsIgnoreCase(method)) {
            doPut(request, response);
        } else {
            User user = createUserFromRequest(request, response);
            if (user == null) return; // Exit if user is null (validation failed)

            try {
                userService.addUser(user);
                response.setStatus(HttpServletResponse.SC_CREATED);
                // Redirection hna khassha tkhdem l7al mn ba3d l'ajout
                response.sendRedirect(request.getContextPath() + "/users");
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to add user.");
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
            return;
        }

        User user = createUserFromRequest(request, response);
        if (user == null) return;

        user.setId(id);

        try {
            userService.updateUser(user);
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
            // Redirect l page dyal users b3d update
            response.sendRedirect(request.getContextPath() + "/users");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while updating the user.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            userService.deleteUser(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content
            response.sendRedirect(request.getContextPath() + "/users"); // Redirection la page dyal users
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while deleting the user");
        }
    }

    private User createUserFromRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password"); // Storing plaintext password
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String roleParam = request.getParameter("role");

        // Validate parameters
        if (username == null || password == null || firstName == null || lastName == null || email == null || roleParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "All fields are required.");
            return null;
        }

        // Handle role conversion
        UserRole role;
        try {
            role = UserRole.valueOf(roleParam.toUpperCase());
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user role.");
            return null; // Return null for validation failure
        }

        return new User(username, password, firstName, lastName, email, role);
    }
}
