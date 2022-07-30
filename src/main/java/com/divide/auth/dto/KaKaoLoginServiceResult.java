package com.divide.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KaKaoLoginServiceResult {
    private Long userId;
    private String email;
    private String password;
}
