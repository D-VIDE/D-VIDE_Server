package com.divide.follow;

import com.divide.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FollowRepository {
    private final EntityManager em;

    public void save(Follow follow) {
        em.persist(follow);
    }

    public List<Follow> getFFFList(User user) {
        List<Follow> followerList = getFollowerList(user);

        return em.createQuery(
                "select f1 " +
                        "from Follow f1 " +
                        "where f1.follower = :follower and f1.followee in :followerList",
                Follow.class
        )
                .setParameter("follower", user)
                .setParameter("followerList", followerList.stream().map(f -> f.getFollower()).toList())
                .getResultList();
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
