package com.divide.auth.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class KakaoLoginRequest {
    @NotNull
    private String code;
}
