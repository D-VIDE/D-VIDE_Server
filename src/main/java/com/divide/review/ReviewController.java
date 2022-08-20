package com.divide.review;

import com.divide.common.CommonReviewResponse;
import com.divide.common.CommonUserResponse;
import com.divide.post.dto.response.Result;
import com.divide.review.dto.request.PostReviewRequest;
import com.divide.review.dto.request.PostReviewRequestV2;
import com.divide.review.dto.response.DeleteReviewLikeResponse;
import com.divide.review.dto.response.GetReviewsResponse;
import com.divide.review.dto.response.GetReviewsResponseV2;
import com.divide.review.dto.response.PostReviewResponse;
import com.divide.user.User;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.divide.review.dto.response.PostReviewLikeResponse;

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

    /**
     * 500m이내 리뷰글 10개 조회 API V2
     *  [GET]http://localhost:8080/api/v2/reviews?longitude=127.0307674032022&latitude=37.4901548250937&offset=0
     *
     */
    @GetMapping("v2/reviews")
    public Result getReviewsV2(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("longitude") double longitude, @RequestParam("latitude") double latitude, @RequestParam(value = "first",
            defaultValue = "0") Integer first){ //json 데이터 확장성을 위해 Result 사용
        List<Review> findReviews = reviewService.findReviewsAll(first, longitude, latitude, 0.5);

        List<GetReviewsResponseV2> collect = findReviews.stream()
                .map( review -> {
                    User user = review.getUser();
                    Boolean isReviewLiked = reviewService.isReviewLiked(userDetails.getUsername(), review);

                    return new GetReviewsResponseV2(
                            new CommonUserResponse(
                                    user.getId(),
                                    user.getNickname(),
                                    user.getProfileImgUrl()
                            ),
                            new CommonReviewResponse(
                                    review.getReviewId(),
                                    review.getPost().getDeliveryLocation().getCoordinate().getX(),
                                    review.getPost().getDeliveryLocation().getCoordinate().getY(),
                                    review.getContent(),
                                    review.getStarRating(),
                                    review.getReviewImages().get(0).getReviewImageUrl(),
                                    review.getPost().getStoreName(),
                                    review.getReviewLikes().size(),
                                    isReviewLiked
                            )
                    );
                })
                .collect(toList());
        return new Result(collect);
    }

    /**
     * 내가 쓴 리뷰 조회 API
     * @param userDetails : 현재 유저를 받아오기 위함
     * @param first : 페이징을 위함
     * @return
     */
    @GetMapping("/v1/reviews/myself")
    public Result getMyReviews(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(value = "first", defaultValue = "0") Integer first){
        List<Review> myReviews = reviewService.findMyReviews(userDetails.getUsername(), first);
        List<GetReviewsResponseV2> collect = myReviews.stream()
                .map( review -> {
                    User user = review.getUser();
                    Boolean isReviewLiked = reviewService.isReviewLiked(userDetails.getUsername(), review);

                    return new GetReviewsResponseV2(
                            new CommonUserResponse(
                                    user.getId(),
                                    user.getNickname(),
                                    user.getProfileImgUrl()
                            ),
                            new CommonReviewResponse(
                                    review.getReviewId(),
                                    review.getPost().getDeliveryLocation().getCoordinate().getX(),
                                    review.getPost().getDeliveryLocation().getCoordinate().getY(),
                                    review.getContent(),
                                    review.getStarRating(),
                                    review.getReviewImages().get(0).getReviewImageUrl(),
                                    review.getPost().getStoreName(),
                                    review.getReviewLikes().size(),
                                    isReviewLiked
                            )
                    );
                })
                .collect(toList());
        return new Result(collect);
    }

    /**
     * 타인이 쓴 리뷰 조회
     * [GET] http://localhost:8080/api/v1/reviews/others?first=0&userId=2
     * @param userDetails : 나의 좋아요 여부를 보기 위한 파라미터
     * @param userId : 타인의 id
     * @param first : 페이징을 위한 값
     * @return
     */
    @GetMapping("/v1/reviews/others")
    public Result getOthersReviews(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Long userId, @RequestParam(value = "first", defaultValue = "0") Integer first){
        List<Review> myReviews = reviewService.findReviewsAllByUserId(userId, first);
        List<GetReviewsResponseV2> collect = myReviews.stream()
                .map( review -> {
                    User user = review.getUser();
                    Boolean isReviewLiked = reviewService.isReviewLiked(userDetails.getUsername(), review);

                    return new GetReviewsResponseV2(
                            new CommonUserResponse(
                                    user.getId(),
                                    user.getNickname(),
                                    user.getProfileImgUrl()
                            ),
                            new CommonReviewResponse(
                                    review.getReviewId(),
                                    review.getPost().getDeliveryLocation().getCoordinate().getX(),
                                    review.getPost().getDeliveryLocation().getCoordinate().getY(),
                                    review.getContent(),
                                    review.getStarRating(),
                                    review.getReviewImages().get(0).getReviewImageUrl(),
                                    review.getPost().getStoreName(),
                                    review.getReviewLikes().size(),
                                    isReviewLiked
                            )
                    );
                })
                .collect(toList());
        return new Result(collect);
    }

    /**
     * 리뷰 좋아요 생성
     * [Post] http://localhost:8080/api/v1/review/3/like?userId=1
     * @param reviewId : 유저가 누른 리뷰의 id
     * @return
     */
    @PostMapping(value = "v1/review/{reviewId}/like")
    public ResponseEntity<PostReviewLikeResponse> reviewLike( @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long reviewId){
        Long newReviewLikeId = reviewService.createReviewLike(userDetails.getUsername(), reviewId);

        return ResponseEntity.status(HttpStatus.CREATED).body( new PostReviewLikeResponse(newReviewLikeId));
    }

    /**
     * 리뷰 좋아요 생성취소
     * [Delete] http://localhost:8080/api/v1/review/{reviewId}/like
     * @param userDetails : 현재 로그인 된 유저
     * @param reviewId : 좋아요 취소를 누른 리뷰의 아이디
     * @return
     */
    @DeleteMapping("v1/review/{reviewId}/like")
    public ResponseEntity<DeleteReviewLikeResponse> reviewLikeCancel(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long reviewId){
        reviewService.cancelReviewLike(userDetails.getUsername(), reviewId);

        return ResponseEntity.status(HttpStatus.OK).body(new DeleteReviewLikeResponse(reviewId));
    }
}
