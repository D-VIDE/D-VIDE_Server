package com.divide.review.dto.response;

import com.divide.review.Review;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetReviewsResponse {
    private Long reviewId;
    private String profileImgUrl;
    private String nickname;
    private double longitude;
    private double latitude;
    private String content;
    private Double starRating;
    private String reviewImgUrl;
//    private int likeCount;
//    private boolean haslike;
    private String storeName;

    public GetReviewsResponse(Review r) {
        this.reviewId = r.getReviewId();
        this.profileImgUrl = r.getUser().getProfileImgUrl();
        this.nickname = r.getUser().getNickname();
        this.longitude = r.getPost().getDeliveryLocation().getCoordinate().getX();
        this.latitude = r.getPost().getDeliveryLocation().getCoordinate().getY();
        this.content = r.getContent();
        this.starRating = r.getStarRating();
        this.reviewImgUrl = r.getReviewImages().get(0).getReviewImageUrl();
//        this.likeCount = ;
        this.storeName = r.getPost().getStoreName();
    }
}
