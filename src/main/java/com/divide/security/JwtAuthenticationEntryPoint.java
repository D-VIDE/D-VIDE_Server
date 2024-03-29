package com.divide.security;

import com.divide.exception.ErrorResponse;
import com.divide.exception.code.AuthErrorCode;
import com.divide.exception.code.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.divide.security.JwtFilter.AUTHORIZATION_HEADER;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
        log.debug("request = " + request + ", response = " + response + ", authException = " + authException);

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorCode errorCode = AuthErrorCode.INVALID_JWT;

        response.setContentType("application/json;charset=utf-8");
        objectMapper.writeValue(response.getWriter(), ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build()
        );
        response.setStatus(errorCode.getHttpStatus().value());
    }

    private String resolveToken(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
