package com.divide.dto.response;

import lombok.Getter;

@Getter
public class SignupResponse {
    private String userId;
    public SignupResponse(String userId) {
        this.userId = userId;
    }
}
