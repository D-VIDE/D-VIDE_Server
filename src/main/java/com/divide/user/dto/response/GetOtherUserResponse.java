package com.divide.user.dto.response;

import com.divide.common.CommonBadgeResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GetOtherUserResponse {
    private String profileImgUrl;
    private String nickname;
    private CommonBadgeResponse badge;
    private Integer followingCount;
    private Integer followerCount;
    private Boolean followed;
}
