package org.capps.repository.implementation;

import jakarta.persistence.*;
import org.capps.repository.UserTokenRepository;
import org.capps.entity.UserToken;


import java.time.LocalDate;

public class UserTokenRepositoryImpl implements UserTokenRepository {

    private EntityManager em;

    public UserTokenRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public UserToken findByUserIdAndDate(int userId, LocalDate date) {
        try {
            return em.createQuery(
                            "SELECT u FROM UserToken u WHERE u.userId = :userId AND u.date = :date", UserToken.class)
                    .setParameter("userId", userId)
                    .setParameter("date", date)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void save(UserToken token){
        em.getTransaction().begin();
        em.persist(token);
        em.getTransaction().commit();
    }

    @Override
    public void update(UserToken token){
        em.getTransaction().begin();
        em.merge(token);
        em.getTransaction().commit();
    }

}
