package com.divide;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class DefaultExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public BaseResponse validException (MethodArgumentNotValidException exception) {
        log.debug(exception.getMessage());
        return new BaseResponse(BaseResponseStatus.REQUEST_ERROR);
    }
}
