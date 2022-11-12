package com.divide.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUserFcmTokenRequest {
    private String fcmToken;
}
