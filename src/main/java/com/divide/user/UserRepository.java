package com.divide.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager em;

    public List<User> getUsers() {
        return em.createQuery("select u from User u", User.class).getResultList();
    }

    public void save(User user) {
        em.persist(user);
    }
    public void save(UserBadge userBadge) {
        em.persist(userBadge);
    }

    public Optional<User> findByEmail(String email){
        try {
            return Optional.of(em.createQuery("select u from User u" +
                            " where u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public User findById(Long id){
        User user = em.find(User.class, id); //postId는 PK
        return user;
    }
}
