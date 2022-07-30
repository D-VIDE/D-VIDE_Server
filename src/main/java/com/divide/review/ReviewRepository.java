package com.divide.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {
    private final EntityManager em;

    public void save(Review review){em.persist(review);}

}
