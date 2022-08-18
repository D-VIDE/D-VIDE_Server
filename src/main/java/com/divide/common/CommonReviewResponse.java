package com.divide.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonReviewResponse {

    private Long reviewId;
    private Double longitude;
    private Double latitude;
    private String content;
    private Double starRating;
    private String reviewImgUrl;
    private String storeName;
//    private int likeCount;
//    private boolean haslike;
}
