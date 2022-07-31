package com.divide.auth;

import com.divide.auth.dto.KaKaoLoginServiceResult;
import com.divide.auth.dto.request.KakaoLoginRequest;
import com.divide.auth.dto.response.KakaoLoginResponse;
import com.divide.auth.dto.response.LoginResponse;
import com.divide.security.JwtFilter;
import com.divide.BaseResponse;
import com.divide.auth.dto.request.LoginRequest;
import com.divide.security.TokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenProvider tokenProvider;

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        String jwt = tokenProvider.createToken(loginRequest.getEmail(), loginRequest.getPassword());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    @GetMapping("/auth/kakaoTest")
    public void kakaoTest(
            @NotNull @RequestParam("code") String code
    ) {
        System.out.println("code = " + code);
    }

    @PostMapping("/auth/kakao")
    public ResponseEntity<KakaoLoginResponse> kakaoLogin(
        @Valid @RequestBody KakaoLoginRequest kakaoLoginRequest
    ) throws JsonProcessingException {
        KaKaoLoginServiceResult kaKaoLoginServiceResult = authService.kakaoLogin(kakaoLoginRequest.getCode(), "http://localhost:8080/api/v1/auth/kakaoTest");
        String jwt = tokenProvider.createToken(kaKaoLoginServiceResult.getEmail(), kaKaoLoginServiceResult.getPassword());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return ResponseEntity.ok(new KakaoLoginResponse(jwt));
    }
}
