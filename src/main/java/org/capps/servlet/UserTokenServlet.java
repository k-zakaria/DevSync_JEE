package org.capps.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.capps.entity.Task;
import org.capps.service.TaskService;
import org.capps.service.UserTokenService;
import org.capps.service.implementation.TaskServiceImpl;
import org.capps.service.implementation.UserTokenServiceImpl;

import java.io.IOException;

public class UserTokenServlet extends HttpServlet {

    private UserTokenService tokenService;
    private TaskService taskService;

    public UserTokenServlet (){
        tokenService = new UserTokenServiceImpl();
        taskService = new TaskServiceImpl();
    }


    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        int taskId = Integer.parseInt(request.getParameter("taskId"));

        // Vérifier si l'utilisateur a des jetons quotidiens
        if (!tokenService.hasTokensLeft(userId)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No tokens available for today.");
            return;
        }

        // Vérifier si l'utilisateur a un jeton mensuel
        if (tokenService.hasMonthlyToken(userId)) {
            // Consommer un jeton quotidien
            if (tokenService.consumeToken(userId)) {
                Task task = taskService.getTaskById(taskId);

                if (task != null) {
                    taskService.deleteTask(taskId);
                    tokenService.useToken(userId, false);
                    response.sendRedirect("tasks");
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Task not found.");
                }
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to consume daily token.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Monthly token exhausted.");
        }
    }

}
