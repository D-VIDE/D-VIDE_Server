package com.divide.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    // 0: success
    // -1: 존재하지 않는 email
    // -2: 틀린 password
    // -3: 빈 email
    // -4: 빈 password
    private int result;
    private String userid;
}
