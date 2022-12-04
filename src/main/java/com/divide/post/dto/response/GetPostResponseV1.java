package com.divide.post.dto.response;

import com.divide.common.CommonPostDetailResponse;
import com.divide.common.CommonUserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Deprecated
public class GetPostResponseV1 {
    private CommonUserResponse user;
    private CommonPostDetailResponse postDetail;
}
