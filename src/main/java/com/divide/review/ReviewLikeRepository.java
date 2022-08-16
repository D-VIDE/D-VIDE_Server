package com.divide.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ReviewLikeRepository {
    private final EntityManager em;

    public void save(ReviewLike reviewLike) {em.persist(reviewLike);}
}
