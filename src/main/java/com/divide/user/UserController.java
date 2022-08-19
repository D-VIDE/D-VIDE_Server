package com.divide.user;

import com.divide.follow.FollowService;
import com.divide.user.dto.response.GetOtherUserResponse;
import com.divide.user.dto.request.SignupRequest;
import com.divide.user.dto.response.GetUserResponse;
import com.divide.user.dto.response.SignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final FollowService followService;

    @PostMapping("/v1/user")
    public ResponseEntity<SignupResponse> signup(@ModelAttribute SignupRequest signupRequest) {
        Long saveId = userService.signup(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SignupResponse(saveId));
    }

    /**
     * 현재 내 정보 조회 API
     * @param userDetails
     * @return
     */
    @GetMapping("/v1/user")
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

    @GetMapping("/v1/user/{id}")
    public GetOtherUserResponse getOtherUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long userId) {
        User user = userService.getOtherUser(userId);
        Integer followerCount = followService.getFollowerCount(user.getEmail());
        Integer followingCount = followService.getFollowingCount(user.getEmail());
        List<String> badgeNameList = user.getBadges().stream().map(userBadge -> userBadge.getBadgeName().getKrName()).toList();
        return GetOtherUserResponse.builder()
                .nickname(user.getNickname())
                .profileImgUrl(user.getProfileImgUrl())
                .badges(badgeNameList)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }
}
