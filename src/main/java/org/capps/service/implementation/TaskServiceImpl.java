package org.capps.service.implementation;

import org.capps.entity.Task;
import org.capps.repository.TaskRepository;
import org.capps.repository.implementation.TaskRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.capps.service.TaskService;

import java.util.List;

public class TaskServiceImpl implements TaskService {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private EntityManager em;
    private TaskRepository taskRepository;


    public TaskServiceImpl() {
        em = emf.createEntityManager();
        taskRepository = new TaskRepositoryImpl(em);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.getAllTasks();
    }

    @Override
    public void addTask(Task task) {
        taskRepository.addTask(task);
    }

    @Override
    public void updateTask(Task task) {
        taskRepository.updateTask(task);
    }

    @Override
    public Task getTaskById(int id) {
        return taskRepository.getTaskById(id);
    }

    @Override
    public void deleteTask(int id) {
        taskRepository.deleteTask(id);
    }
}
