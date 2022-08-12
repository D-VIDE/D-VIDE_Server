package com.divide.post.dto.response;

import com.divide.common.CommonPostResponse;
import com.divide.common.CommonUserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetPostsResponse {
    private CommonUserResponse user;
    private CommonPostResponse post;
}
