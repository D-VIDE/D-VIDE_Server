package com.divide.auth;

import com.divide.security.JwtFilter;
import com.divide.security.TokenProvider;
import com.divide.BaseResponse;
import com.divide.auth.dto.request.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthService authService;


    @PostMapping("/auth/login")
    public ResponseEntity<BaseResponse> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword());

        Authentication authentication = authenticationManagerBuilder
                .getObject()
                .authenticate(usernamePasswordAuthenticationToken);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity(new BaseResponse(jwt), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/auth/kakao")
    public void kakaoLogin(
        @Nullable @PathParam("code") String code
    ) throws JsonProcessingException {
        if (code != null) {
            authService.kakaoLogin(code);
        }

        return;
    }
}
