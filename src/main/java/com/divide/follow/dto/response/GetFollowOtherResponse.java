package com.divide.follow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class GetFollowOtherResponse {
    private Long userId;
    private String profileImgUrl;
    private String nickname;
    private Boolean followed;
}
