package com.divide.review;

import com.divide.post.PostRepository;
import com.divide.post.domain.Direction;
import com.divide.post.domain.Location;
import com.divide.post.domain.Post;
import com.divide.review.dto.request.PostReviewRequest;
import com.divide.user.User;
import com.divide.user.UserRepository;
import com.divide.utils.GeometryUtil;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    private final EntityManager em;

    /**
     * 리뷰글 생성
     */
    @Transactional
    public Long review(Long userId, Long postId, PostReviewRequest request ) throws ParseException {
        //엔티티 조회
        User user = userRepository.findById(userId);
        Post post = postRepository.findByPostId(postId);

        //storeLocation: double -> point로 변환
        String pointWKT = String.format("POINT(%s %s)", request.getLongitude(), request.getLatitude());
        Point point = (Point) new WKTReader().read(pointWKT);

        //리뷰 생성
        Review review = Review.builder()
                .user(user)
                .post(post)
                .starRating(request.getStarRating())
                .content(request.getContent())
                .build();

        reviewRepository.save(review);
        return review.getReviewId();
    }


    /**
     * 리뷰글 거리기반 조회
     * @param longitude : 기준좌표 x
     * @param latitude  : 기준좌표 y
     * @param distance  : 기준 좌표 x,y로 부터 distanceKM 떨어진 모든 범위
     *
     */
    public List<Review> getNearbyReviews(Double latitude, Double longitude, Double distance, int offset) {
        String pointFormat = getPointFormat(latitude, longitude, distance);
        Query query = em.createNativeQuery("SELECT * FROM review,post WHERE MBRContains(ST_LINESTRINGFROMTEXT(" + pointFormat + ", post.delivery_location)", Review.class)
                .setFirstResult(offset)
                .setMaxResults(10);
        List<Review> reviewLists = query.getResultList();
        return reviewLists;
    }

    private String getPointFormat(Double latitude, Double longitude, Double distance) {
        //일정 거리 범위 내에있는 좌표들을 비교하기 위해서 MBR이 필요
        //MBR을 구하기 위해 북동쪽, 남서쪽 좌표 구하기
        Location northEast = GeometryUtil
                .calculate(latitude, longitude, distance, Direction.NORTHEAST.getBearing());
        Location southWest = GeometryUtil
                .calculate(latitude, longitude, distance, Direction.SOUTHWEST.getBearing());
        // 기준 좌표의 북동쪽으로 nKM에 위치한 좌표 : x1, y1
        double x1 = northEast.getLatitude();
        double y1 = northEast.getLongitude();
        // 기준 좌표의 남서쪽으로 nKM에 위치한 좌표 : x2, y2
        double x2 = southWest.getLatitude();
        double y2 = southWest.getLongitude();

        //기준 좌표 x,y로 부터 distanceKM 떨어진 모든 범위의 delivery_location 데이터를 조회하는 쿼리
        String pointFormat = String.format("'LINESTRING(%f %f, %f %f)')", x1, y1, x2, y2);
        return pointFormat;
    }

}
