package com.divide.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetUserResponse {
    private Long id;
    private String nickname;
}
