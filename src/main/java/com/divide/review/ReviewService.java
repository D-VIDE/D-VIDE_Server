package com.divide.review;

import com.divide.post.PostRepository;
import com.divide.post.domain.Post;
import com.divide.review.dto.request.PostReviewRequest;
import com.divide.user.User;
import com.divide.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

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
                .storeLocation(point)
                .content(request.getContent())
                .build();

        reviewRepository.save(review);
        return review.getReviewId();
    }

}
