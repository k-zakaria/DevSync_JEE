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
        request.setAttribute("tasks", taskService.getAllTasks());
        request.setAttribute("tags", tagService.getAllTags());
        request.setAttribute("users", userService.getAllUsers());
        request.getRequestDispatcher("/views/tasks.jsp").forward(request, response);
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
        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            LocalDate startDate = LocalDate.parse(request.getParameter("start_date"));
            LocalDate endDate = LocalDate.parse(request.getParameter("end_date"));
            StatusTask status = StatusTask.valueOf(request.getParameter("status").toUpperCase());
            int userId = Integer.parseInt(request.getParameter("user_id"));
            String[] tagIds = request.getParameterValues("tag_id");

            if (title == null || description == null || tagIds == null || endDate.isAfter(startDate.plusDays(3))) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input data.");
                return null;
            }

            User user = new User();
            user.setId(userId);

            List<Tag> tags = new ArrayList<>();
            for (String tagId : tagIds) {
                Tag tag = new Tag();
                tag.setId(Integer.parseInt(tagId));
                tags.add(tag);
            }

            Task task = new Task(title, description, startDate, endDate, status, user);
            task.setTags(tags);
            return task;
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error processing task data.");
            return null;
        }
    }
}
