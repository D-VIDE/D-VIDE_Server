package com.divide.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewLikeErrorCode implements ErrorCode{
    DUPLICATED_LIKE(HttpStatus.BAD_REQUEST, "Only one review like is allowed per post.");

    private final HttpStatus httpStatus;
    private final String message;
}