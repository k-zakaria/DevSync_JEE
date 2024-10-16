package org.capps.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.capps.entity.User;
import org.capps.service.UserService;
import org.capps.service.implementation.UserServiceImpl;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserService userService;

    public LoginServlet() {
        this.userService = new UserServiceImpl(); // Initialisation du service
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("views/login.jsp").forward(request,response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer les paramètres email et password de la requête
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Appeler le service pour valider les informations de l'utilisateur
        User user = userService.validateLogin(email, password);

        if (user != null) {
            // Si l'utilisateur est trouvé, créer une session et y stocker les informations de l'utilisateur
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Rediriger vers la page du tableau de bord après une connexion réussie
            response.sendRedirect("/DevSync");
        } else {
            // Si la validation échoue, renvoyer à la page de login avec un message d'erreur
            request.setAttribute("error", "Email ou mot de passe incorrect.");
            request.getRequestDispatcher("views/login.jsp").forward(request,response);
        }
    }


}
