package com.divide.review;

import com.divide.post.PostService;
import com.divide.review.dto.request.postReviewRequest;
import com.divide.review.dto.response.postReviewResponse;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰 생성 API - 임시
     */
    @PostMapping(value = "/review")
    public ResponseEntity<postReviewResponse> review(@RequestBody postReviewRequest request, @RequestParam Long userId, @RequestParam Long postId) throws ParseException {
        Long newReviewId = reviewService.review(userId, postId, request);

        return ResponseEntity.ok().body(new postReviewResponse(newReviewId));
    }
}
