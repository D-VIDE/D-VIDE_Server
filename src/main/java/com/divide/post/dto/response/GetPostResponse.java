package com.divide.post.dto.response;

import com.divide.common.CommonPostDetailResponse;
import com.divide.common.CommonUserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
public class GetPostResponse {
    private CommonUserResponse user;
    private CommonPostDetailResponse postDetail;
}
