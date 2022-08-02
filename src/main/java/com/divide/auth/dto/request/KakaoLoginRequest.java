package com.divide.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class KakaoLoginRequest {
    @NotNull
    private String code;
}
