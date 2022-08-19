package com.divide.follow.dto.request;

import lombok.Getter;

@Getter
public class GetFollowResponseWithRelation extends GetFollowResponse {
    private Boolean isFFF;

    public GetFollowResponseWithRelation(Long userId, String profileImgUrl, String nickname, Boolean isFFF) {
        super(userId, profileImgUrl, nickname);
        this.isFFF = isFFF;
    }
}
