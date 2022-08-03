package com.divide.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    INACTIVE_USER(HttpStatus.FORBIDDEN, "User is inactive"),
    INVALID_EMAIL(HttpStatus.FORBIDDEN, "User is not registered"),
    DUPLICATED_USER(HttpStatus.BAD_REQUEST, "User is already signed");
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
