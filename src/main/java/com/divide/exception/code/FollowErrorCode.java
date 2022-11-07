package com.divide.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FollowErrorCode implements ErrorCode {
    DUPLICATED_FOLLOW(HttpStatus.BAD_REQUEST, "Already followed"),
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "Follow is not found"),
    FOLLOW_SELF_ERROR(HttpStatus.BAD_REQUEST, "Self follow is not permitted"),
    FOLLOW_ACCESS_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Not authorized access to follow"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
