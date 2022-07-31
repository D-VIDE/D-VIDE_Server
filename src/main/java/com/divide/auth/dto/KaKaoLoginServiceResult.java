package com.divide.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KaKaoLoginServiceResult {
    private String email;
    private String password;
}
