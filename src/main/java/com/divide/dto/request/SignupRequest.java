package com.divide.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignupRequest {
    private String email;
    private String password;
    private String profileImgUrl;
    private String name;
    private String nickname;
}
