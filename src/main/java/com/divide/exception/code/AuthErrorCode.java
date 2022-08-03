package com.divide.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "Invalid jwt"),
    FAILED_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "Failed to authentication"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
