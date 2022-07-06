package com.divide.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginRequest {
    private String email;
    private String password;
}
