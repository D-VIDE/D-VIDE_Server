package com.divide.auth;

import com.divide.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public void kakaoLogin(final String authorizationCode) throws JsonProcessingException {
        /* 카카오 토큰 발급 */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "f5c8e321f9869cdbc2362fe45c4556a0");
        params.add("redirect_uri", "http://localhost:8080/api/v1/auth/kakao");
        params.add("code", authorizationCode);

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

        json = objectMapper.readValue(response.getBody(), Map.class);
        Map kakao_account = (Map) json.get("kakao_account");
        Map profile = (Map) kakao_account.get("profile");
        String nickname = (String) profile.get("nickname");
        String imageUrl = (String) profile.get("thumbnail_image_url");
        String email = (String) kakao_account.get("email");
        System.out.println("nickname = " + nickname);
        System.out.println("imageUrl = " + imageUrl);
        System.out.println("email = " + email);
    }
}
