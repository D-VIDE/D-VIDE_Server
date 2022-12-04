package com.divide.post.dto.response;

import com.divide.common.CommonPostDetailResponse;
import com.divide.common.CommonUserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public class GetPostResponseV2 {
    private CommonUserResponse user;
    private CommonPostDetailResponse postDetail;
    private Boolean ordered;
}
