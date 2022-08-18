package com.divide.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class GetOtherUserResponse {
    private String profileImgUrl;
    private String nickname;
    private List<String> badges;
    private Integer followingCount;
    private Integer followerCount;
}
