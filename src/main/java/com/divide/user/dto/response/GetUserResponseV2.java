package com.divide.user.dto.response;

import com.divide.common.CommonBadgeResponse;
import com.divide.common.CommonLocationResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetUserResponseV2 {
    private String email;
    private String nickname;
    private String profileImgUrl;
    private CommonBadgeResponse badge;
    private Integer followerCount;
    private Integer followingCount;
    private Integer savedPrice;
    private CommonLocationResponse location;
}
