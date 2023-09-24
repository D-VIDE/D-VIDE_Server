package com.divide.review;

import com.divide.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReviewLikeRepository {
    private final EntityManager em;

    public void save(ReviewLike reviewLike) {
        em.persist(reviewLike);
    }

    public void delete(ReviewLike reviewLike){
        em.remove(reviewLike);
    }

    public ReviewLike findByReviewLikeId(Long reviewLikeId){
        ReviewLike reviewLike = em.find(ReviewLike.class, reviewLikeId);
        return reviewLike;
    }
    public Optional<ReviewLike> findByUserIdAndReviewId(Long userId, Long reviewId){
        Optional<ReviewLike> reviewLike = null;
        try{
            reviewLike = Optional.ofNullable(em.createQuery("select rl from ReviewLike rl where rl.user.id =:userId and rl.review.reviewId =: reviewId" , ReviewLike.class)
                    .setParameter("userId", userId)
                    .setParameter("reviewId", reviewId)
                    .getSingleResult());
        }catch (NoResultException e){
            reviewLike = Optional.empty();
        }
        return reviewLike;
    }

    public void removeAllByUser(User user) {
        em.createQuery("delete from ReviewLike rl where rl.user = :user")
                .setParameter("user", user)
                .executeUpdate();
    }
}
