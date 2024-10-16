package org.capps.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;
import org.capps.entity.Tag;
import org.capps.entity.Task;
import org.capps.entity.StatusTask;
import org.capps.entity.User;
import org.capps.service.TagService;
import org.capps.service.TaskService;
import org.capps.service.UserService;
import org.capps.service.implementation.TagServiceImpl;
import org.capps.service.implementation.TaskServiceImpl;
import org.capps.service.implementation.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
        taskService = new TaskServiceImpl();
        tagService = new TagServiceImpl();
        userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int userId = user.getId();

        if (taskService == null || tagService == null || userService == null) {
            throw new IllegalStateException("Un ou plusieurs services ne sont pas initialisés.");
        }

        List<Task> tasks = taskService.getAllTasks(userId);
        List<Tag> tags = tagService.getAllTags();
        List<User> users = userService.getAllUsers();

        request.setAttribute("tasks", tasks);
        request.setAttribute("tags", tags);
        request.setAttribute("users", users);

        request.getRequestDispatcher("views/tasks.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");

        if (method == null) {
            Task task = createTaskFromRequest(request, response);
            if (task != null) {
                taskService.addTask(task);
                response.sendRedirect(request.getContextPath() + "/tasks");
            }
        } else {
            switch (method.toUpperCase()) {
                case "DELETE":
                    processDelete(request, response);
                    break;
                case "PUT":
                    processPut(request, response);
                    break;
                case "UPDATE_STATUS":
                    Task updatedTask = updateStatus(request, response);
                    if (updatedTask != null) {
                        response.sendRedirect(request.getContextPath() + "/tasks");
                    }
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported operation.");
            }
        }
    }

    private Task updateStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            StatusTask newStatus = StatusTask.valueOf(request.getParameter("status").toUpperCase());
            Task task = taskService.getTaskById(taskId);

            if (task == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Task not found.");
                return null;
            }

            task.setStatus(newStatus);
            taskService.updateTask(task);
            return task;
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid task ID or status.");
            return null;
        }
    }

    private void processPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Task task = createTaskFromRequest(request, response);
            if (task != null) {
                task.setId(id);
                taskService.updateTask(task);
                response.sendRedirect(request.getContextPath() + "/tasks");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid task ID.");
        }
    }

    private void processDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            taskService.deleteTask(id);
            response.sendRedirect(request.getContextPath() + "/tasks");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid task ID.");
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

        if (title == null || description == null || startDateParam == null || endDateParam == null || statusParam == null || userIdParam == null || tagIdParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "All fields are required.");
            return null;
        }
        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(startDateParam);
            endDate = LocalDate.parse(endDateParam);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format.");
            return null;
        }
        StatusTask status;
        try {
            status = StatusTask.valueOf(statusParam.toUpperCase());
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid task status.");
            return null;
        }

        User user = new User();
        user.setId(Integer.parseInt(userIdParam));


        List<Tag> tags = new ArrayList<>();
        for (String tagId : tagIdParam) {
            Tag tag = new Tag();
            tag.setId(Integer.parseInt(tagId));
            tags.add(tag);
        }

        Task task = new Task(title, description, startDate, endDate, status, user);
        task.setTags(tags);
        return task;
    }
}
