package com.divide.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ReviewLikeRepository {
    private final EntityManager em;

    public void save(ReviewLike reviewLike) {
        em.persist(reviewLike);}

    public void delete(ReviewLike reviewLike){
        em.remove(reviewLike);
    }

    public ReviewLike findById(Long reviewLikeId){
        ReviewLike reviewLike = em.find(ReviewLike.class, reviewLikeId);
        return reviewLike;
    }

}
