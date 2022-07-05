package com.divide.post.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPostResponse {
    private Long postId;

    public NewPostResponse(Long postId) {
        this.postId = postId;
    }
}
