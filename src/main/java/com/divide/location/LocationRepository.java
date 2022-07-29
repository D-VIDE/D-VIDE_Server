package com.divide.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocationRepository {
    private final EntityManager em;

    public void save(Location location) {
        em.persist(location);
    }

    public Optional<Location> findById(Long id) {
        return Optional.of(em.find(Location.class, id));
    }

    public List<Location> findAll() {
        return em.createQuery("select l from Location l", Location.class)
                .getResultList();
    }
}
