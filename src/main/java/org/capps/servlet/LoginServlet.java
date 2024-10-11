package org.capps.servlet;

import jakarta.servlet.http.HttpServlet;
import org.capps.service.UserService;

public class LoginServlet extends HttpServlet {

    private UserService userService;

    public LoginServlet(UserService userService) {
        this.userService = userService;
    }

    

}
