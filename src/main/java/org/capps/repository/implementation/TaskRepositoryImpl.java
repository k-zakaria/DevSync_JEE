package org.capps.repository.implementation;

import org.capps.entity.Task;
import jakarta.persistence.EntityManager;
import org.capps.repository.TaskRepository;

import java.util.List;

public class TaskRepositoryImpl implements TaskRepository {

    private EntityManager em;

    public TaskRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Task> getAllTasks() {
        return em.createQuery("SELECT t FROM Task t", Task.class).getResultList();
    }

    @Override
    public void addTask(Task task) {
        em.getTransaction().begin();
        em.persist(task);
        em.getTransaction().commit();
    }

    @Override
    public void updateTask(Task task) {
        em.getTransaction().begin();
        em.merge(task);
        em.getTransaction().commit();
    }

    @Override
    public Task getTaskById(int id) {
        return em.find(Task.class, id);
    }

    @Override
    public void deleteTask(int id) {
        em.getTransaction().begin();
        Task task = em.find(Task.class, id);
        if (task != null) {
            em.remove(task);
        }
        em.getTransaction().commit();
    }
}
