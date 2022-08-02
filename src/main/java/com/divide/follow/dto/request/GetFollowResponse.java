package com.divide.follow.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetFollowResponse {
    List<GetFollowResponseElement> followList;

    @AllArgsConstructor
    @Getter
    public static class GetFollowResponseElement {
        private Long userId;
        private String profileImgUrl;
        private String nickname;
        private String relation;
    }
}
