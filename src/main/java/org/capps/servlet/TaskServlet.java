package org.capps.servlet;

import jakarta.servlet.annotation.WebServlet;
import org.capps.entity.Tag;
import org.capps.entity.Task;
import org.capps.entity.StatusTask;
import org.capps.entity.User;
import org.capps.service.TagService;
import org.capps.service.TaskService;
import org.capps.service.UserService;
import org.capps.service.implementation.TagServiceImpl;
import org.capps.service.implementation.TaskServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.capps.service.implementation.UserServiceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/tasks")
public class TaskServlet extends HttpServlet {

    private TaskService taskService;
    private TagService tagService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        taskService = new TaskServiceImpl();
        tagService = new TagServiceImpl();
        userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Task> tasks = taskService.getAllTasks();
        request.setAttribute("tasks", tasks);

        List<Tag> tags = tagService.getAllTags();
        request.setAttribute("tags", tags);

        List<User> users = userService.getAllUsers(); // Appel à la méthode d'instance
        request.setAttribute("users", users); // Assurez-vous que vous utilisez le même nom que dans la JSP

        request.getRequestDispatcher("/views/tasks.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");

        if ("DELETE".equalsIgnoreCase(method)) {
            doDelete(request, response);
        } else if ("PUT".equalsIgnoreCase(method)) {
            doPut(request, response);
        } else if ("UPDATE_STATUS".equalsIgnoreCase(method)) {
            Task updatedTask = updateStatus(request, response); // Appel de la méthode updateStatus
            if (updatedTask == null) return; // Vérifier si la mise à jour a échoué
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect(request.getContextPath() + "/tasks");
        } else {
            Task task = createTaskFromRequest(request, response);
            if (task == null) return;

            try {
                taskService.addTask(task);
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.sendRedirect(request.getContextPath() + "/tasks");
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to add task.");
            }
        }
    }


    private Task updateStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String taskIdParam = request.getParameter("taskId");
        String statusParam = request.getParameter("status");

        // Vérification des paramètres
        if (taskIdParam == null || statusParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Task ID and Status are required.");
            return null;
        }

        int taskId;
        try {
            taskId = Integer.parseInt(taskIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Task ID format.");
            return null;
        }

        // Récupérer la tâche par ID
        Task task = taskService.getTaskById(taskId);

        if (task == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Task not found.");
            return null;
        }

        try {
            // Convertir le statut en enum
            StatusTask newStatus = StatusTask.valueOf(statusParam.toUpperCase());
            task.setStatus(newStatus);
            taskService.updateTask(task);

            return task; // Ici, la tâche mise à jour est retournée
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid task status.");
            return null;
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid task ID");
            return;
        }

        Task task = createTaskFromRequest(request, response);
        if (task == null) return;

        task.setId(id);

        try {
            taskService.updateTask(task);
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect(request.getContextPath() + "/tasks");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while updating the task.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            taskService.deleteTask(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            response.sendRedirect(request.getContextPath() + "/tasks");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid task ID");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while deleting the task.");
        }
    }

    private Task createTaskFromRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String startDateParam = request.getParameter("start_date");
        String endDateParam = request.getParameter("end_date");
        String statusParam = request.getParameter("status");
        String userIdParam = request.getParameter("user_id");
        String[] tagIdParam = request.getParameterValues("tag_id"); // Récupérer les IDs des tags en tant que tableau

        // Validate parameters
        if (title == null || description == null || startDateParam == null ||
                endDateParam == null || statusParam == null || userIdParam == null || tagIdParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "All fields are required.");
            return null;
        }

        // Handle date conversion
        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(startDateParam);
            endDate = LocalDate.parse(endDateParam);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format.");
            return null;
        }

        //Validation : Limiter à 3 jours après la date de début
        if (endDate.isAfter(startDate.plusDays(3))) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "La tâche ne peut pas être planifiée à plus de 3 jours après la date de début.");
            return null;
        }

        // Handle status conversion
        StatusTask status;
        try {
            status = StatusTask.valueOf(statusParam.toUpperCase());
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid task status.");
            return null;
        }

        // Fetch user (Remplace cette partie avec la logique de récupération réelle)
        User user = new User();
        user.setId(Integer.parseInt(userIdParam));

        // Créer une collection de tags
        List<Tag> tags = new ArrayList<>();
        for (String tagId : tagIdParam) {
            Tag tag = new Tag();
            tag.setId(Integer.parseInt(tagId));
            tags.add(tag);
        }

        // Créer et retourner la tâche
        Task task = new Task(title, description, startDate, endDate, status, user);
        task.setTags(tags); // Assurez-vous que la classe Task a bien cette méthode

        return task;
    }

}
