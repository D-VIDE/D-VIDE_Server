package com.divide.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {
    POST_NOT_RECRUITING(HttpStatus.CONFLICT, "Post's status is not recruiting");

    private final HttpStatus httpStatus;
    private final String message;
}
