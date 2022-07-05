package com.divide.config.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetUserResponse {
    private Long id;
    private String nickname;
}
