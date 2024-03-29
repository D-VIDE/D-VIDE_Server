package com.divide.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@AllArgsConstructor
public class UserBadgeRepository {
    private final EntityManager em;

    public void save(UserBadge userBadge) {
        em.persist(userBadge);
    }

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

    public void removeAllByUser(User user) {
        user.updateSelectedBadge(null);
        em.flush();
        em.clear();
        em.createQuery("delete from UserBadge ub where ub.user = :user")
                .setParameter("user", user)
                .executeUpdate();
    }
}
