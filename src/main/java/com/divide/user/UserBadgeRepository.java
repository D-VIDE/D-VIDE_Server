package com.divide.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@AllArgsConstructor
public class UserBadgeRepository {
    private final EntityManager em;

    public List<UserBadge> findByUser(User user) {
        return em.createQuery("select ub from UserBadge as ub where ub.user = :user", UserBadge.class)
                .setParameter("user", user)
                .getResultList();
    }

    public UserBadge findByUserAndBadgeName(User user, UserBadge.BadgeName badgeName) {
        return em.createQuery("select ub from UserBadge as ub where ub.user = :user and ub.badgeName = :badgeName", UserBadge.class)
                .setParameter("user", user)
                .setParameter("badgeName", badgeName)
                .getSingleResult();
    }
}
