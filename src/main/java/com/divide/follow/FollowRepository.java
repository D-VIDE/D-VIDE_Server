package com.divide.follow;

import com.divide.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FollowRepository {
    private final EntityManager em;

    public void save(Follow follow) {
        em.persist(follow);
    }

    public Optional<Follow> find(User follower, User followee) {
        try {
            return Optional.ofNullable(em.createQuery("select f from Follow f where f.follower = :follower and f.followee = :followee", Follow.class)
                    .setParameter("follower", follower)
                    .setParameter("followee", followee)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public void remove(Follow follow) {
        em.remove(follow);
    }
    public List<Follow> getFollowingList(User user) {
        return em.createQuery("select f from Follow f where f.follower = :user", Follow.class)
                .setParameter("user", user)
                .getResultList();
    }

    public List<Follow> getFollowerList(User user) {
        return em.createQuery("select f from Follow f where f.followee = :user", Follow.class)
                .setParameter("user", user)
                .getResultList();
    }

    public Integer getFollowingCount(User user) {
        return em.createQuery("select count(f) from Follow f where f.follower = :user", Long.class)
                .setParameter("user", user)
                .getSingleResult()
                .intValue();
    }

    public Integer getFollowerCount(User user) {
        return em.createQuery("select count(f) from Follow f where f.followee = :user", Long.class)
                .setParameter("user", user)
                .getSingleResult()
                .intValue();
    }
}
