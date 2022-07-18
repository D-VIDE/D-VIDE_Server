package com.divide.security;

import com.divide.BaseResponse;
import com.divide.BaseResponseStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
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
        String jwt = resolveToken(request);
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401

        log.debug("request = " + request + ", response = " + response + ", authException = " + authException);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=utf-8");

        ObjectMapper objectMapper = new ObjectMapper();
        boolean isFailedToLogin = authException instanceof BadCredentialsException;
        boolean isEmptyJwt = StringUtils.hasText(jwt) == false;
        if (isFailedToLogin) {
            objectMapper.writeValue(response.getWriter(), new BaseResponse(BaseResponseStatus.FAILED_TO_LOGIN));
        } else if (isEmptyJwt) {
                objectMapper.writeValue(response.getWriter(), new BaseResponse(BaseResponseStatus.EMPTY_JWT));
        } else {
            objectMapper.writeValue(response.getWriter(), new BaseResponse(BaseResponseStatus.INVALID_JWT));
        }
    }

    private String resolveToken(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
