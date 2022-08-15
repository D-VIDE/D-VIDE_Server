package com.divide.review;

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
                        "WHERE MBRContains( ST_LINESTRINGFROMTEXT(:pointFormat), p.delivery_location)", Review.class )
                .setFirstResult(first)
                .setParameter("pointFormat", pointFormat)
                .setMaxResults(10);

        List<Review> reviewLists = query.getResultList();
        return reviewLists;
    }

}
