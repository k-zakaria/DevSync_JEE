package org.capps.servlet;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.capps.service.TaskChangeRequestService;
import org.capps.service.implementation.TaskChangeRequestServiceImpl;

import java.io.IOException;

@WebServlet("/taskChangeRequestServelet")
public class TaskChangeRequestServlet extends HttpServlet {
    private TaskChangeRequestService requestService;

    public TaskChangeRequestServlet(){
        requestService = new TaskChangeRequestServiceImpl();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws SecurityException, IOException{
        int userId = Integer.parseInt(request.getParameter("userId"));
        int taskId = Integer.parseInt(request.getParameter("taskId"));

        requestService.createRequest(userId, taskId);

        if (requestService.isRequestPendingOver12Hours(userId)){
            requestService.doubleUserTokens(userId);
        }

        response.sendRedirect("tasks");
    }
}
