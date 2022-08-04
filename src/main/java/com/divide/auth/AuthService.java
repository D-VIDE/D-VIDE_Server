package com.divide.auth;

import com.divide.auth.dto.KaKaoLoginServiceResult;
import com.divide.user.User;
import com.divide.user.UserRepository;
import com.divide.user.UserRole;
import com.divide.utils.OCIUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public KaKaoLoginServiceResult kakaoLogin(final String authorizationCode, final String callbackUrl) throws JsonProcessingException {
        String kakaoAccessToken = getKakaoAccessToken(authorizationCode, callbackUrl);
        Map kakaoUserMap = getKakaoUser(kakaoAccessToken);
        Map profile = (Map) kakaoUserMap.get("profile");

        String email = (String) kakaoUserMap.get("email");
        String password = email + "kakaoLogin";
        String profileImgUrl = saveProfileImgFromUrl((String) profile.get("thumbnail_image_url"));
        String nickname = (String) profile.get("nickname");

        if (userRepository.findByEmail(email).isEmpty()) {
            userRepository.signup(new User(email, passwordEncoder.encode(password), profileImgUrl, nickname, UserRole.USER));
        }

        return new KaKaoLoginServiceResult(email, password);
    }

    private String getKakaoAccessToken(final String authorizationCode, final String callbackUrl) throws JsonProcessingException {
        /* 카카오 토큰 발급 */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "f5c8e321f9869cdbc2362fe45c4556a0");
        params.add("redirect_uri", callbackUrl);
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
        return (String)json.get("access_token");
    }

    private Map getKakaoUser(final String accessToken) throws JsonProcessingException {
        /* 카카오 토큰으로 정보 가져오기 */
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format("Bearer %s", accessToken));
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        Map json = objectMapper.readValue(response.getBody(), Map.class);
        return (Map) json.get("kakao_account");
    }

    private String saveProfileImgFromUrl(final String kakaoProfileImgUrl) {
        String sourceFileExtension = StringUtils.getFilenameExtension(kakaoProfileImgUrl).toLowerCase();

        String destinationFileName = UUID.randomUUID() + "." + sourceFileExtension;
        return OCIUtil.uploadProfileImgFromUrl(kakaoProfileImgUrl, destinationFileName);
    }
}
