package com.divide.order;

import com.divide.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public List<Order> findOrders(User user, Integer first) {
        return em.createQuery("select o from Order as o " +
                        "join fetch o.user " +
                        "join fetch o.post " +
                        "where o.user = :user order by o.createdAt")
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
}
