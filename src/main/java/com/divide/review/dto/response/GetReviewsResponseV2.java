package com.divide.review.dto.response;

import com.divide.common.CommonReviewResponse;
import com.divide.common.CommonUserResponse;
import com.divide.review.Review;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetReviewsResponseV2 {
    private CommonUserResponse user;
    private CommonReviewResponse review;
}
