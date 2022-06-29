package com.divide.dto.request;

import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String profileImgUrl;
    private String name;
    private String nickname;
    private String address;
}
