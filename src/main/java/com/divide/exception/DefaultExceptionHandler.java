package com.divide.exception;

import com.divide.exception.code.AuthErrorCode;
import com.divide.exception.code.CommonErrorCode;
import com.divide.exception.code.ErrorCode;
import com.divide.exception.code.UserErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 우리가 직접 던져줄 Exception을 컨트롤함.
     * @param e
     * @return
     */
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handleCustomException(RestApiException e) {
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    /**
     * 잘못 된 Argument가 들어왔을 때(예를 들면, int형인데 string으로 온다거나?) 발생하는 Exception을 컨트롤함.
     * @param e
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("handleIllegalArgument", e);
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, e.getMessage());
    }

    /**
     * RequestPart에 Validation에 실패할 경우 뜨는 Exception을 컨트롤함.
     * @param e the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            MissingServletRequestPartException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        log.warn("handleMissingServletRequestPart", e);
        System.out.println("e.getRequestPartName() = " + e.getRequestPartName());
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(e, errorCode);
    }

    /**
     * RequestBody에 Valid 어노테이션을 통해서 validation에 실패했을 때 뜨는 Exception을 컨트롤함.
     * @param e the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return handleBindException(e, headers, status, request);
    }

    /**
     * RequestParam에 Valid 어노테이션을 통해서 validation에 실패했을 때 뜨는 Exception을 컨트롤함.
     * @param e the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return
     */
    @Override
    public ResponseEntity<Object> handleBindException(
            BindException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        log.warn("handleBindException", e);
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(e, errorCode);
    }

    /**
     * 정의되지 않은 URL로 들어온 경우 발생하는 Exception을 컨트롤함.
     * @param e the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        log.warn("handleNoSuchMethodException", e);
        ErrorCode errorCode = CommonErrorCode.UNDEFINED_REQUEST_URL;
        return handleExceptionInternal(errorCode);
    }

    /**
     * 정의되지 않은 Method로 들어온 경우 발생하는 Exception을 컨트롤함.
     * @param e the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        log.warn("handleHttpRequestMethodNotSupported", e);
        ErrorCode errorCode = CommonErrorCode.UNDEFINED_REQUEST_METHOD;
        return handleExceptionInternal(errorCode);
    }

    /**
     * Validated 어노테이션으로 발생하는 Exception을 컨트롤함.
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("handleConstraintViolationException", e);
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(e, errorCode);
    }

    /**
     * 잘못된 id/pw로 로그인 하는 경우 발생하는 Exception을 컨트롤함.
     * @param e
     * @return
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException e) {
        log.warn("handleBadCredentialsException", e);

        ErrorCode errorCode = AuthErrorCode.FAILED_AUTHENTICATION;
        return handleExceptionInternal(errorCode);
    }

    /**
     * 삭제된 유저의 jwt로 조회 하는 등 jwt에 있는 email로 유저 조회가 안 되는 경우 발생하는 Exception을 컨트롤함.
     * @param e
     * @return
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.warn("handleUsernameNotFoundException", e);

        ErrorCode errorCode = UserErrorCode.INVALID_EMAIL;
        return handleExceptionInternal(errorCode);
    }

    /**
     * DB Constraints 위반 시 발생하는 Exception을 컨트롤함.
     * @param e
     * @return
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.warn("handleDataIntegrityViolationException", e);
        ErrorCode errorCode = CommonErrorCode.INVALID_REQUEST;
        return handleExceptionInternal(errorCode);
    }

    /**
     * 그 외 모든 Exception을 컨트롤함.
     * Stacktrace를 찍어서 debugging
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception ex) {
        log.warn("handleAllException", ex);
        ex.printStackTrace();

        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(errorCode);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, message));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(message)
                .build();
    }

    private ResponseEntity<Object> handleExceptionInternal(BindException e, ErrorCode errorCode) {
        List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ErrorResponse.ValidationError::of)
                .collect(Collectors.toList());

        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, validationErrorList));
    }

    private ResponseEntity<Object> handleExceptionInternal(MissingServletRequestPartException e, ErrorCode errorCode) {
        String invalidPartName = e.getRequestPartName();
        FieldError fieldError = new FieldError(invalidPartName, invalidPartName, "널이어서는 안됩니다");
        List<ErrorResponse.ValidationError> validationErrorList = List.of(ErrorResponse.ValidationError.of(fieldError));
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, validationErrorList));
    }

    private ResponseEntity<Object> handleExceptionInternal(ConstraintViolationException e, ErrorCode errorCode) {
        String errorMessage = e.getMessage();
        String[] split = errorMessage.split(",");
        List<ErrorResponse.ValidationError> validationErrorList = Arrays.stream(split).map(str -> {
            String[] split1 = str.split(":");
            String field = split1[0].substring(split1[0].lastIndexOf(".") + 1);
            String message = split1[1].strip();
            return ErrorResponse.ValidationError.of(new FieldError(field, field, message));
        }).toList();

        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, validationErrorList));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode, List<ErrorResponse.ValidationError> validationErrorList) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .errors(validationErrorList)
                .build();
    }
}
