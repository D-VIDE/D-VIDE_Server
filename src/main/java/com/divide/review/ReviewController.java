package com.divide.review;

import com.divide.post.dto.response.Result;
import com.divide.review.dto.request.PostReviewRequest;
import com.divide.review.dto.request.PostReviewRequestV2;
import com.divide.review.dto.response.GetReviewsResponse;
import com.divide.review.dto.response.PostReviewResponse;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰 생성 API V1
     * [POST] http://localhost:8080/api/v1/review/?postId=4
     * @param postId : 리뷰가 생성되는 게시글
     * @param request : 게시글 생성시 필요한 request
     * @param reviewImageFiles : 리뷰 이미지 파일들
     * @return
     * @throws ParseException
     */
    @PostMapping(value = "v1/review")
    public ResponseEntity<PostReviewResponse> createReview(@RequestParam Long postId, @AuthenticationPrincipal UserDetails userDetails, @RequestPart PostReviewRequest request, @RequestPart List<MultipartFile> reviewImageFiles) throws ParseException {
        Long newReviewId = reviewService.createReview( postId, userDetails.getUsername(), request, reviewImageFiles);

        return ResponseEntity.ok().body(new PostReviewResponse(newReviewId));
    }

    /**
     * 리뷰 생성 API V2
     * [POST] http://localhost:8080/api/v2/review
     * @param request : 게시글 생성시 필요한 request
     * @param reviewImageFiles : 리뷰 이미지 파일들
     * @return
     * @throws ParseException
     */
    @PostMapping(value = "v2/review")
    public ResponseEntity<PostReviewResponse> createReviewV2(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestPart PostReviewRequestV2 request, @RequestPart List<MultipartFile> reviewImageFiles) throws ParseException {
        Long newReviewId = reviewService.createReviewV2( userDetails.getUsername(), request, reviewImageFiles);

        return ResponseEntity.status(HttpStatus.CREATED).body(new PostReviewResponse(newReviewId));
    }

    /**
     * 500m이내 리뷰글 10개 조회 API
     *  [GET] http://localhost:8080/api/v1/reviews/nearby?longitude=37.49015482509&latitude=127.030767490&offset=0
     *
     */
    @GetMapping("v1/reviews")
    public Result getReviews(@RequestParam("longitude") double longitude, @RequestParam("latitude") double latitude, @RequestParam(value = "first",
            defaultValue = "0") Integer first){ //json 데이터 확장성을 위해 Result 사용
        List<Review> findReviews = reviewService.findReviewsAll(first, longitude, latitude, 0.5);

        List<GetReviewsResponse> collect = findReviews.stream()
                .map( r -> new GetReviewsResponse(r))
                .collect(toList());
        return new Result(collect);
    }
}
