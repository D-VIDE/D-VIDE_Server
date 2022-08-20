package com.divide.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommonReviewDetailResponse {

    private Long reviewId;
    private Double longitude;
    private Double latitude;
    private String content;
    private Double starRating;
    private List<String> reviewImgUrl;
    private String storeName;
    private Integer likeCount;
    private Boolean isLiked;
}
