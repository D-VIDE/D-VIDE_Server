package com.divide.review;

import com.divide.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {
    private final EntityManager em;

    public void save(Review review){em.persist(review);}

    public List<Review> findReviewsAll(Integer first, String pointFormat) {
        Query query = em.createNativeQuery("SELECT r.* FROM review r " +
                        "JOIN post p on p.post_id = r.post_id " +
                        "WHERE MBRContains( ST_LINESTRINGFROMTEXT(:pointFormat), p.delivery_location) " +
                        "ORDER BY r.created_at DESC", Review.class )
                .setFirstResult(first)
                .setParameter("pointFormat", pointFormat)
                .setMaxResults(10);

        List<Review> reviewLists = query.getResultList();
        return reviewLists;
    }

    public List<Review> findReviewsAllByUserId(Integer first, Long user_id){
        Query query = em.createNativeQuery("SELECT r.* FROM review r " +
                "JOIN post p on p.post_id = r.post_id " +
                "WHERE r.user_id = :user_id ORDER BY r.created_at DESC", Review.class)
                .setFirstResult(first)
                .setParameter("user_id", user_id)
                .setMaxResults(10);

        List<Review> reviewLists = query.getResultList();
        return reviewLists;
    }


    public List<Review> findReviewsAllByStoreName(Integer first, String storeName){
        Query query = em.createNativeQuery("SELECT r.* FROM review r " +
                        "JOIN post p on p.post_id = r.post_id " +
                        "WHERE p.store_name = :storeName ORDER BY r.created_at DESC", Review.class)
                .setFirstResult(first)
                .setParameter("storeName", storeName)
                .setMaxResults(10);

        List<Review> reviewLists = query.getResultList();
        return reviewLists;
    }

    public Review findById(Long reviewId){
        Review review = em.find(Review.class, reviewId);
        return review;
    }

    public List<Review>findReviewsByStarRating(Integer first){
        Query query = em.createNativeQuery("SELECT r.* FROM review r " +
                        "JOIN post p on p.post_id = r.post_id " +
                        "ORDER BY r.star_rating DESC", Review.class)
                .setFirstResult(first)
                .setMaxResults(5);

        List<Review> reviewLists = query.getResultList();
        return reviewLists;
    }

    public void setNullAllByUser(User user) {
        em.flush();
        em.clear();
        em.createQuery("update Review r set r.user = null where r.user = :user")
                .setParameter("user", user)
                .executeUpdate();
    }
}
