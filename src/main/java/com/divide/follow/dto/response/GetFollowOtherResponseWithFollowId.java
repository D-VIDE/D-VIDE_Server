package com.divide.follow.dto.response;

import lombok.Getter;

@Getter
public class GetFollowOtherResponseWithFollowId extends GetFollowOtherResponse {
    private Long followId;

    public GetFollowOtherResponseWithFollowId(Long userId, String profileImgUrl, String nickname, Boolean followed,
                                              Long followId) {
        super(userId, profileImgUrl, nickname, followed);
        this.followId = followId;
    }
}
