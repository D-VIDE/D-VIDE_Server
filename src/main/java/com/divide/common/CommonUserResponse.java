package com.divide.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonUserResponse {
    private Long id;
    private String nickname;
    private String profileImgUrl;
}
