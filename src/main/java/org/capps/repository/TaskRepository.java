package org.capps.repository;

import org.capps.entity.Task;
import java.util.List;

public interface TaskRepository {
    List<Task> getTasksByUserId(int userId);
    void addTask(Task task);
    void updateTask(Task task);
    Task getTaskById(int id);
    void deleteTask(int id);
}
