package com.divide.review;

import com.divide.post.domain.Post;
import com.divide.post.dto.response.GetNearbyPostsResponse;
import com.divide.post.dto.response.Result;
import com.divide.review.dto.request.PostReviewRequest;
import com.divide.review.dto.response.GetNearbyReviewResponse;
import com.divide.review.dto.response.PostReviewResponse;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰 생성 API - 임시
     */
    @PostMapping(value = "/review")
    public ResponseEntity<PostReviewResponse> review(@RequestBody PostReviewRequest request, @RequestParam Long userId, @RequestParam Long postId) throws ParseException {
        Long newReviewId = reviewService.review(userId, postId, request);

        return ResponseEntity.ok().body(new PostReviewResponse(newReviewId));
    }

    /**
     * 500m이내 리뷰글 10개 조회 API
     *  [GET] http://localhost:8080/api/v1/reviews/nearby?longitude=37.49015482509&latitude=127.030767490&offset=0
     *
     */
    @GetMapping("/reviews/nearby")
    public Result findNearbyPosts(@RequestParam("longitude") double longitude, @RequestParam("latitude") double latitude, @RequestParam(value = "offset",
            defaultValue = "0") int offset){ //json 데이터 확장성을 위해 Result 사용
        List<Review> findReviews = reviewService.getNearbyReviews(longitude, latitude, 0.5, offset);

        List<GetNearbyReviewResponse> collect = findReviews.stream()
                .map( r -> new GetNearbyReviewResponse(r))
                .collect(toList());
        return new Result(collect);
    }
}
