package com.divide.user;

import com.divide.follow.FollowService;
import com.divide.user.dto.request.PostUserBadgeRequest;
import com.divide.user.dto.response.*;
import com.divide.user.dto.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final FollowService followService;

    /**
     * 회원가입 API
     * @param signupRequest
     * @return
     */
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
    public GetUserResponse getUserV1(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        Integer followerCount = followService.getFollowerCount(userDetails.getUsername());
        Integer followingCount = followService.getFollowingCount(userDetails.getUsername());
        CommonBadgeResponse badgeResponse = new CommonBadgeResponse(
                user.getSelectedBadge().getBadgeName().getKrName(),
                user.getSelectedBadge().getBadgeName().getDescription());

        return GetUserResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImgUrl(user.getProfileImgUrl())
                .badge(badgeResponse)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .savedPrice(user.getSavedMoney())
                .build();
    }

    /**
     * 타인의 정보 조회 API
     * @param userDetails
     * @param userId
     * @return
     */
    @GetMapping("/v1/user/{id}")
    public GetOtherUserResponse getOtherUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long userId) {
        User user = userService.getUserById(userId);
        Integer followerCount = followService.getFollowerCount(user.getEmail());
        Integer followingCount = followService.getFollowingCount(user.getEmail());
        CommonBadgeResponse badgeResponse = new CommonBadgeResponse(
                user.getSelectedBadge().getBadgeName().getKrName(),
                user.getSelectedBadge().getBadgeName().getDescription());
        return GetOtherUserResponse.builder()
                .nickname(user.getNickname())
                .profileImgUrl(user.getProfileImgUrl())
                .badge(badgeResponse)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }

    /**
     * 현재 유저의 badge 정보 조회 API
     * @param userDetails
     * @return
     */
    @GetMapping("/v1/user/badges")
    public GetUserBadgeResponse getUserBadge(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        List<CommonBadgeResponse> badges = userService.getBadgeList(user).stream()
                .map(ub -> new CommonBadgeResponse(
                        ub.getBadgeName().getKrName(),
                        ub.getBadgeName().getDescription()))
                .toList();
        return GetUserBadgeResponse.builder()
                .badges(badges)
                .build();
    }

    /**
     * 대표 badge 선택 API
     * TODO: exception 재정의
     * @param userDetails
     * @param postUserBadgeRequest
     * @return
     */
    @PostMapping("/v1/user/badge")
    public ResponseEntity postUserBadge(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PostUserBadgeRequest postUserBadgeRequest
    ) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        List<UserBadge> badgeList = userService.getBadgeList(user);
        Optional<UserBadge.BadgeName> badgeName = badgeList.stream()
                .map(UserBadge::getBadgeName)
                .filter(name -> name.getKrName().equals(postUserBadgeRequest.getBadgeName()))
                .findFirst();

        userService.updateSelectedBadge(user, badgeName.orElseThrow());

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
