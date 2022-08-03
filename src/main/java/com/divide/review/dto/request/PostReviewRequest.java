package com.divide.review.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostReviewRequest {
    private double starRating;
    private String content;
    private String storeName;
//    private List<String> reveiwImages;

    private double longitude;
    private double latitude;
}
