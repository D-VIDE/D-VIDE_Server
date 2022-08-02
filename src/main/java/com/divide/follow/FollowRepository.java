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
        List<Follow> followingList = getFollowingList(user);

        return em.createQuery(
                "select f1.follower " +
                        "from Follow f1 " +
                        "where f1.follower = :follower and f1.id in :followingList"
        )
                .setParameter("follower", user)
                .setParameter("followingList", followingList.stream().map(Follow::getId).collect(Collectors.toList()))
                .getResultList();
    }

    public List<Follow> getFollowingList(User user) {
        return em.createQuery("select f from Follow f where f.follower = :user")
                .setParameter("user", user)
                .getResultList();
    }

    public List<Follow> getFollowerList(User user) {
        return em.createQuery("select f from Follow f where f.followee = :user")
                .setParameter("user", user)
                .getResultList();
    }
}
