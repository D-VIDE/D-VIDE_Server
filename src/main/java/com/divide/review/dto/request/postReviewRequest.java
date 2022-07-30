package com.divide.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class postReviewRequest {
    private double starRating;
    private String content;
    private String storeName;
//    private List<String> reveiwImages;

    private double longitude;
    private double latitude;
}
