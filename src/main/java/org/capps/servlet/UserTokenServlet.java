package org.capps.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.capps.service.TaskService;
import org.capps.service.UserTokenService;
import org.capps.service.implementation.TaskServiceImpl;
import org.capps.service.implementation.UserTokenServiceImpl;

import java.io.IOException;

@WebServlet("/userTokenServlet")
public class UserTokenServlet extends HttpServlet {

    private UserTokenService tokenService;
    private TaskService taskService;

    public UserTokenServlet (){
        tokenService = new UserTokenServiceImpl();
        taskService = new TaskServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));

        int tokensUsed = tokenService.getTokensUsed(userId);
        request.setAttribute("tokensUsed", tokensUsed);

        request.getRequestDispatcher("/tasks").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int userId = Integer.parseInt(request.getParameter("userId"));

        // Vérifie si l'utilisateur effectue une suppression ou un remplacement
        if (request.getParameter("taskId") != null) {
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            handleTaskDeletion(userId, taskId, response);
        } else
            if ("replace".equals(request.getParameter("dailyTokenAction"))) {
            handleTaskReplacement(userId, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action invalide.");
        }
    }

    // Suppression d'une tâche
    private void handleTaskDeletion(int userId, int taskId, HttpServletResponse response) throws IOException {

            if (tokenService.checkAndRecordDeletion(userId)) {
                taskService.deleteTask(taskId);  // Supprimer la tâche
                response.sendRedirect("tasks");  // Redirection après succès
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Impossible de consommer un jeton.");
            }
    }

    // Remplacement d'une tâche avec un jeton journalier
    private void handleTaskReplacement(int userId, HttpServletResponse response) throws IOException {
        if (tokenService.hasTokensLeft(userId)) {
            // Logique de remplacement ici (par exemple : modifier l'état ou réassigner)
            tokenService.useToken(userId, false);  // Consommer un jeton journalier
            response.sendRedirect("tasks");  // Redirection après succès
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Aucun jeton journalier disponible.");
        }
    }



}
