package com.divide.user;

import com.divide.follow.FollowService;
import com.divide.user.dto.request.SignupRequest;
import com.divide.user.dto.response.GetUserResponse;
import com.divide.user.dto.response.SignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final FollowService followService;

    @PostMapping("/user")
    public ResponseEntity<SignupResponse> signup(@ModelAttribute SignupRequest signupRequest) {
        Long saveId = userService.signup(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SignupResponse(saveId));
    }

    /**
     * 현재 자신의 정보
     */
    @GetMapping("/user")
    public GetUserResponse getUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        Integer followerCount = followService.getFollowerCount(userDetails.getUsername());
        Integer followingCount = followService.getFollowingCount(userDetails.getUsername());
        List<String> badgeNameList = user.getBadges().stream().map(userBadge -> userBadge.getBadgeName().getKrName()).toList();
        return GetUserResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImgUrl(user.getProfileImgUrl())
                .badges(badgeNameList)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .savedPrice(user.getSavedMoney())
                .build();
    }
}
