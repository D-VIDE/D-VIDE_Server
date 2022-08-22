package com.divide.review.dto.response;

import com.divide.common.CommonReviewResponse;
import com.divide.common.CommonUserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetRecommendsResponse {
    private String storeName;
    private String reviewImgUrl;
}
