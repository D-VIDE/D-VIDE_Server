package com.divide.follow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetFollowResponse {
    private Long userId;
    private String profileImgUrl;
    private String nickname;
}
