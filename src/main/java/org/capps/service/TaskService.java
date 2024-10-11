package org.capps.service;

import org.capps.entity.Task;
import java.util.List;

public interface TaskService {
    List<Task> getAllTasks();
    void addTask(Task task);
    void updateTask(Task task);
    Task getTaskById(int id);
    void deleteTask(int id);
}
