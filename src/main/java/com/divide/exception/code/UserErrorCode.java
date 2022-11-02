package com.divide.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    INACTIVE_USER(HttpStatus.FORBIDDEN, "User is inactive"),
    INVALID_EMAIL(HttpStatus.FORBIDDEN, "User is not registered"),
    DUPLICATED_USER(HttpStatus.FORBIDDEN, "User is already signed"),
    OTHER_USER_IS_ME(HttpStatus.BAD_REQUEST, "Requested other user is me"),
    INVALID_BADGE_NAME(HttpStatus.BAD_REQUEST, "BadgeName is not valid"),
    ALREADY_SET_BADGE_NAME(HttpStatus.FORBIDDEN, "BadgeName is already set as request"),
    ALREADY_REGISTERED_BADGE(HttpStatus.FORBIDDEN, "Badge is already registered");


    private final HttpStatus httpStatus;
    private final String message;
}
