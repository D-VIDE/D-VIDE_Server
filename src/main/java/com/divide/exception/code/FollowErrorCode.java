package com.divide.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FollowErrorCode implements ErrorCode {
    FOLLOW_NOT_FOUND(HttpStatus.FORBIDDEN, "Follow is not found"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
