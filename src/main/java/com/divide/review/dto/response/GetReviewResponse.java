package com.divide.review.dto.response;

import com.divide.common.CommonReviewDetailResponse;
import com.divide.common.CommonReviewResponse;
import com.divide.common.CommonUserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetReviewResponse {
    private CommonUserResponse user;
    private CommonReviewDetailResponse reviewDetail;
}
