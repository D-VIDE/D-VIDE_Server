package com.divide.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class SignupRequest {
    @NotNull @Email
    private String email;
    @NotNull
    private String password;

    private String profileImgUrl;
    @NotNull
    private String nickname;
}
