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

    @PostMapping("/login")
    public ResponseEntity login(
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
            System.out.println("code = " + code);

            /* 카카오 토큰 발급 */
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", "f5c8e321f9869cdbc2362fe45c4556a0");
            params.add("redirect_uri", "http://localhost:8080/api/v1/auth/kakao");
            params.add("code", code);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            RestTemplate rt = new RestTemplate();

            ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    entity,
                    String.class
                );
            ObjectMapper objectMapper = new ObjectMapper();
            Map json = objectMapper.readValue(response.getBody(), Map.class);
            String accessToken = (String)json.get("access_token");

            /* 카카오 토큰으로 정보 가져오기 */
            headers.clear();
            headers.add("Authorization", String.format("Bearer %s", accessToken));
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            response = rt.exchange(
                    "https://kapi.kakao.com//v2/user/me",
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            System.out.println("response = " + response);
        }

        return;
    }
}
