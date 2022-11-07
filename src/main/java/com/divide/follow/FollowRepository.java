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

    public Optional<Follow> findById(Long followId) {
        try {
            return Optional.ofNullable(em.createQuery("select f from Follow f where f.id = :followId", Follow.class)
                    .setParameter("followId", followId)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public void remove(Follow follow) {
        em.remove(follow);
    }
    public List<Follow> getFollowingList(User user, Integer first) {
        return em.createQuery("select f from Follow f where f.follower = :user order by f.createdAt desc", Follow.class)
                .setParameter("user", user)
                .setFirstResult(first)
                .setMaxResults(30)
                .getResultList();
    }

    public List<Follow> getFollowerList(User user, Integer first) {
        return em.createQuery("select f from Follow f where f.followee = :user order by f.createdAt desc", Follow.class)
                .setParameter("user", user)
                .setFirstResult(first)
                .setMaxResults(30)
                .getResultList();
    }

    public Integer getFollowingCount(User user) {
        return em.createQuery("select count(f) from Follow f where f.follower = :user", Long.class)
                .setParameter("user", user)
                .getSingleResult()
                .intValue();
    }

    @Deprecated
    public Boolean checkFFF(Follow follow) {
        return em.createQuery("select count(f) > 0 from Follow f where f.followee = :other and f.follower = :me", Boolean.class)
                .setParameter("other", follow.getFollower())
                .setParameter("me", follow.getFollowee())
                .getSingleResult();
    }

    public Integer getFollowerCount(User user) {
        return em.createQuery("select count(f) from Follow f where f.followee = :user", Long.class)
                .setParameter("user", user)
                .getSingleResult()
                .intValue();
    }
}
