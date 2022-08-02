package com.divide.user.dto.response;

import com.divide.badge.Badge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class GetUserResponse {
    private String email;
    private String nickname;
    private String profileImgUrl;
    private List<Badge> badges;
    private Integer followerCount;
    private Integer followingCount;
    private Integer savedPrice;
}
