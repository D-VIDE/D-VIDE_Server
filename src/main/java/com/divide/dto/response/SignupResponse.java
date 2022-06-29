package com.divide.dto.response;

import lombok.Getter;

@Getter
public class SignupResponse extends CommonResponse {
    private String userId;
    public SignupResponse(String result, String userId) {
        super(result);
        this.userId = userId;
    }
}
