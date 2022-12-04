package com.divide.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FcmErrorCode implements ErrorCode {
    FCM_ERROR(HttpStatus.BAD_REQUEST, "Requesting Fcm is not processed."),
    FCM_PARSING_ERROR(HttpStatus.FORBIDDEN, "Parsing Fcm result failed"),
    FCM_CODE_NOT_AVAILABLE(HttpStatus.FORBIDDEN, "FCM code was not set"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
