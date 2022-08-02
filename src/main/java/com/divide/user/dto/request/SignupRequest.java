package com.divide.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
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
