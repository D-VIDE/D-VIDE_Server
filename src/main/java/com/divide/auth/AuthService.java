package com.divide.auth;

import com.divide.auth.dto.KaKaoLoginServiceResult;
import com.divide.user.User;
import com.divide.user.UserBadge;
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

    public KaKaoLoginServiceResult kakaoLogin(final String accessToken) throws JsonProcessingException {
        Map kakaoUserMap = getKakaoUser(accessToken);
        Map profile = (Map) kakaoUserMap.get("profile");

        String email = (String) kakaoUserMap.get("email");
        String password = email + "kakaoLogin";

        if (userRepository.findByEmail(email).isEmpty()) {
            String profileImgUrl = saveProfileImgFromUrl(email, (String) profile.get("thumbnail_image_url"));
            String nickname = (String) profile.get("nickname");
            User user = new User(email, passwordEncoder.encode(password), profileImgUrl, nickname, UserRole.USER);
            userRepository.save(user);
        }

        return new KaKaoLoginServiceResult(email, password);
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

    private String saveProfileImgFromUrl(String email, String kakaoProfileImgUrl) {
        String sourceFileExtension = StringUtils.getFilenameExtension(kakaoProfileImgUrl).toLowerCase();
        return OCIUtil.uploadProfileImgFromUrl(kakaoProfileImgUrl, email + "/" + UUID.randomUUID() + "." + sourceFileExtension);
    }
}
