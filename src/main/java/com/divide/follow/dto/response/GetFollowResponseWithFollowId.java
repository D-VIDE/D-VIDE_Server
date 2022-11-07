package com.divide.follow.dto.response;

import lombok.Getter;

@Getter
public class GetFollowResponseWithFollowId extends GetFollowResponse{
    private Long followId;

    public GetFollowResponseWithFollowId(Long userId, String profileImgUrl, String nickname, Long followId) {
        super(userId, profileImgUrl, nickname);
        this.followId = followId;
    }
}
