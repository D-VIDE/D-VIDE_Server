package com.divide.order;

import com.divide.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public Optional<Order> findById(Long orderId) {
        return Optional.ofNullable(em.find(Order.class, orderId));
    }

    public List<Order> findOrders(User user, Integer first) {
        return em.createQuery("select o from Order as o " +
                        "join fetch o.user " +
                        "join fetch o.post " +
                        "where o.user = :user order by o.createdAt desc")
                .setParameter("user", user)
                .setFirstResult(first)
                .setMaxResults(10)
                .getResultList();
    }

    public void save(Order order) {
        em.persist(order);
    }
    public void save(OrderImage orderImage) {
        em.persist(orderImage);
    }

    public List<Order> findOrdersByPostId(Long postId) {
        return em.createQuery("select o from Order as o "
                        + "join fetch o.user "
                        + "where o.post.postId = :postId", Order.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public void setNullAllByUser(User user) {
        em.flush();
        em.clear();
        em.createQuery("update Order o set o.user = null where o.user = :user")
                .setParameter("user", user)
                .executeUpdate();
    }
}
