package com.divide.auth.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoLoginResponse {
    private final String token;
    private final Long userId;
}
