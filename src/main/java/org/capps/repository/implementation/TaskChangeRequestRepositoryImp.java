package org.capps.repository.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.capps.entity.TaskChangeRequest;
import org.capps.repository.TaskChangeRequestRepository;

import java.util.Optional;

public class TaskChangeRequestRepositoryImp implements TaskChangeRequestRepository {

    private EntityManager em;

    public TaskChangeRequestRepositoryImp(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<TaskChangeRequest> findPendingRequestByUserId(int userId) {
        try {
            String jpql = "SELECT r FROM TaskChangeRequest r WHERE r.user.id = :userId AND r.status = 'PENDING'";
            TypedQuery<TaskChangeRequest> query = em.createQuery(jpql, TaskChangeRequest.class);
            query.setParameter("userId", userId);

            // Récupérer une seule demande ou retourner une valeur vide
            return query.getResultStream().findFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public void save(TaskChangeRequest request) {
        try {
            em.getTransaction().begin();
            em.persist(request);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
