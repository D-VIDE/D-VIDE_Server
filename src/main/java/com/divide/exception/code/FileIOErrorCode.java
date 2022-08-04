package com.divide.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FileIOErrorCode implements ErrorCode{
    OCI_ERROR(HttpStatus.CONFLICT, "OCI server is something wrong"),
    KAKAO_ERROR(HttpStatus.CONFLICT, "Kakao server is something wrong"),
    FILE_IO_ERROR(HttpStatus.CONFLICT, "Exception occur on handle file");
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
