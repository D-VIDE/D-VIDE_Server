package com.divide.review;

import com.divide.exception.RestApiException;
import com.divide.exception.code.ReviewLikeErrorCode;
import com.divide.post.PostRepository;
import com.divide.post.domain.Direction;
import com.divide.post.domain.Location;
import com.divide.post.domain.Post;
import com.divide.review.dto.request.PostReviewRequest;
import com.divide.review.dto.request.PostReviewRequestV2;
import com.divide.user.User;
import com.divide.user.UserRepository;
import com.divide.utils.GeometryUtil;
import com.divide.utils.OCIUtil;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /**
     * 리뷰글 생성
     */
    @Transactional
    public Long createReview(Long postId, String userEmail, PostReviewRequest request, List<MultipartFile> reviewImageFiles) throws ParseException {
        //엔티티 조회
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(""));
        Post post = postRepository.findByPostId(postId);

        //리뷰 이미지 생성
        List<ReviewImage> reviewImages= new ArrayList<ReviewImage>();
        for(MultipartFile reviewImageFile: reviewImageFiles){
            String storeName = request.getStoreName();
            String extension = StringUtils.getFilenameExtension(reviewImageFile.getOriginalFilename()).toLowerCase();
            String reviewImageUrl = OCIUtil.uploadFile(reviewImageFile, OCIUtil.FolderName.REVIEW, storeName + "/" + UUID.randomUUID() + "." + extension);
            reviewImages.add(ReviewImage.create(reviewImageUrl));
        }

        //리뷰 생성
        Review review = Review.builder()
                .user(user)
                .post(post)
                .starRating(request.getStarRating())
                .content(request.getContent())
                .reviewImages(reviewImages)
                .build();

        reviewRepository.save(review);
        return review.getReviewId();
    }

    /**
     * 리뷰글 생성 V2
     */
    @Transactional
    public Long createReviewV2(String userEmail, PostReviewRequestV2 request, List<MultipartFile> reviewImageFiles) throws ParseException {
        //엔티티 조회
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(""));
        Post post = postRepository.findByPostId(request.getPostId());

        //리뷰 이미지 생성
        List<ReviewImage> reviewImages= new ArrayList<ReviewImage>();
        for(MultipartFile reviewImageFile: reviewImageFiles){
            String storeName = post.getStoreName();
            String extension = StringUtils.getFilenameExtension(reviewImageFile.getOriginalFilename()).toLowerCase();
            String reviewImageUrl = OCIUtil.uploadFile(reviewImageFile, OCIUtil.FolderName.REVIEW, storeName + "/" + UUID.randomUUID() + "." + extension);
            reviewImages.add(ReviewImage.create(reviewImageUrl));
        }

        //리뷰 생성
        Review review = Review.builder()
                .user(user)
                .post(post)
                .starRating(request.getStarRating())
                .content(request.getContent())
                .reviewImages(reviewImages)
                .build();

        reviewRepository.save(review);
        return review.getReviewId();
    }

    /**
     * 리뷰글 생성 Test
     */
    @Transactional
    public Long createReviewTest(String userEmail, Double starRating, String content, Long postId, List<String> reviewImgUrls) throws ParseException {
        //엔티티 조회
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(""));
        Post post = postRepository.findByPostId(postId);

        //리뷰 생성
        Review review = Review.builder()
                .user(user)
                .post(post)
                .starRating(starRating)
                .content(content)
                .reviewImages(reviewImgUrls.stream().map(reviewImgUrl -> ReviewImage.create(reviewImgUrl)).toList())
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
    public List<Review> findReviewsAll(Integer first, Double longitude, Double latitude,Double distance) {
        String pointFormat = getPointFormat(longitude, latitude, distance);

        return reviewRepository.findReviewsAll(first, pointFormat);
    }

    private String getPointFormat(Double longitude, Double latitude, Double distance) {
        //일정 거리 범위 내에있는 좌표들을 비교하기 위해서 MBR이 필요
        //MBR을 구하기 위해 북동쪽, 남서쪽 좌표 구하기
        Location northEast = GeometryUtil
                .calculate(latitude, longitude, distance, Direction.NORTHEAST.getBearing());
        Location southWest = GeometryUtil
                .calculate(latitude, longitude, distance, Direction.SOUTHWEST.getBearing());
        // 기준 좌표의 북동쪽으로 nKM에 위치한 좌표 : x1, y1
        double x1 = northEast.getLongitude();
        double y1 = northEast.getLatitude();
        // 기준 좌표의 남서쪽으로 nKM에 위치한 좌표 : x2, y2
        double x2 = southWest.getLongitude();
        double y2 = southWest.getLatitude();

        //기준 좌표 x,y로 부터 distanceKM 떨어진 모든 범위의 delivery_location 데이터를 조회하는 쿼리
        String pointFormat = String.format("LINESTRING(%f %f, %f %f)", x1, y1, x2, y2);
        return pointFormat;
    }

    /**
     * 내 리뷰글 조회
     * @param userEmail
     * @param first
     * @return
     */
    public List<Review> findMyReviews(String userEmail, Integer first){
        //현재 유저 조회
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(""));

        return reviewRepository.findReviewsAllByUserId(first, user.getId());
    }

    public List<Review> findReviewsAllByUserId(Long userId, Integer first){
        return reviewRepository.findReviewsAllByUserId( first, userId);
    }

    public List<Review> findReviewsAllByStoreName(String storeName, Integer first){
        return reviewRepository.findReviewsAllByStoreName( first, storeName);
    }

    public Review findReview(Long reviewId){
        return reviewRepository.findById(reviewId);
    }

    private boolean validateDuplicateReviewLike(ReviewLike reviewLike){
        Optional<ReviewLike> DbReviewLike = reviewLikeRepository.findByUserIdAndReviewId(reviewLike.getUser().getId(), reviewLike.getReview().getReviewId());
        return DbReviewLike.isPresent();
    }

    //리뷰 좋아요
    @Transactional
    public Long createReviewLike(String userEmail, Long reviewId){
        //엔티티 조회
        User currentUser = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(""));
        Review review = reviewRepository.findById(reviewId);

        //리뷰 좋아요 생성
        ReviewLike reviewLike = ReviewLike.builder()
                .user(currentUser)
                .review(review)
                .build();

        //리뷰 좋아요 중복 체크
        if(validateDuplicateReviewLike(reviewLike)){
            throw new RestApiException(ReviewLikeErrorCode.DUPLICATED_LIKE);
        }

        reviewLikeRepository.save(reviewLike);
        return reviewLike.getReviewLikeId();
    }

    @Transactional
    public void cancelReviewLike(String userEmail, Long reviewId){
        //게시글 좋아요 조회
        Review review = reviewRepository.findById(reviewId);
        List<ReviewLike> reviewLikes = review.getReviewLikes();
//        reviewLikes.removeIf(reviewLike -> reviewLike.getUser().getEmail().equals(userEmail));

        for(ReviewLike reviewLike: reviewLikes){
            if ( reviewLike.getUser().getEmail().equals(userEmail) ){
                reviewLikeRepository.delete(reviewLike);
            }
        }
    }

    public Boolean isReviewLiked(String userEmail, Review review){
        //현재 유저 조회
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(""));

        Optional<ReviewLike> reviewLike = reviewLikeRepository.findByUserIdAndReviewId(user.getId(), review.getReviewId());
        if(reviewLike.equals(Optional.empty()) ){
            return false;
        }else{
            return true;
        }
    }
}
