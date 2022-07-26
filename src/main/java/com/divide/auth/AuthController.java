package com.divide.auth;

import com.divide.BaseResponseStatus;
import com.divide.auth.dto.request.KakaoLoginRequest;
import com.divide.auth.dto.response.KakaoLoginResponse;
import com.divide.security.JwtFilter;
import com.divide.BaseResponse;
import com.divide.auth.dto.request.LoginRequest;
import com.divide.security.TokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenProvider tokenProvider;

    @PostMapping("/auth/login")
    public ResponseEntity<BaseResponse> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        String jwt = tokenProvider.createToken(loginRequest.getEmail(), loginRequest.getPassword());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity(new BaseResponse(jwt), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/auth/kakaoTest")
    public void kakaoTest(
            @NotNull @RequestParam("code") String code
    ) {
        System.out.println("code = " + code);
    }

    @PostMapping("/auth/kakao")
    public ResponseEntity<BaseResponse> kakaoLogin(
        @Valid @RequestBody KakaoLoginRequest kakaoLoginRequest
    ) throws JsonProcessingException {

        KakaoLoginResponse kakaoLoginResponse = authService.kakaoLogin(kakaoLoginRequest.getCode(), "http://localhost:8080/api/v1/auth/kakaoTest");
        String jwt = tokenProvider.createToken(kakaoLoginResponse.getEmail(), kakaoLoginResponse.getPassword());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity(new BaseResponse(jwt), httpHeaders, HttpStatus.OK);
    }
}
