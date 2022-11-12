package com.divide.user;

import com.divide.common.CommonBadgeResponse;
import com.divide.common.CommonLocationResponse;
import com.divide.exception.RestApiException;
import com.divide.exception.code.UserErrorCode;
import com.divide.follow.FollowService;
import com.divide.user.dto.request.PostUserBadgeRequest;
import com.divide.user.dto.request.PostUserFcmTokenRequest;
import com.divide.user.dto.request.PostUserLocationRequest;
import com.divide.user.dto.response.*;
import com.divide.user.dto.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
     * 현재 내 정보 조회 API V1
     * @param userDetails
     * @return
     */
    @GetMapping("/v1/user")
    public GetUserResponseV1 getUserV1(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        Integer followerCount = followService.getFollowerCount(userDetails.getUsername());
        Integer followingCount = followService.getFollowingCount(userDetails.getUsername());
        CommonBadgeResponse badgeResponse = new CommonBadgeResponse(
                user.getSelectedBadge().getBadgeName().getKrName(),
                user.getSelectedBadge().getBadgeName().getDescription());

        return GetUserResponseV1.builder()
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
     * 현재 내 정보 조회 API V2
     * @param userDetails
     * @return
     */
    @GetMapping("/v2/user")
    public GetUserResponseV2 getUserV2(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        Integer followerCount = followService.getFollowerCount(userDetails.getUsername());
        Integer followingCount = followService.getFollowingCount(userDetails.getUsername());
        CommonBadgeResponse badgeResponse = new CommonBadgeResponse(
                user.getSelectedBadge().getBadgeName().getKrName(),
                user.getSelectedBadge().getBadgeName().getDescription());

        CommonLocationResponse locationResponse = null;
        if (user.getLocation() != null) {
            locationResponse = new CommonLocationResponse(
                    user.getLocation().getLatitude(),
                    user.getLocation().getLongitude()
            );
        }

        return GetUserResponseV2.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImgUrl(user.getProfileImgUrl())
                .badge(badgeResponse)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .savedPrice(user.getSavedMoney())
                .location(locationResponse)
                .build();
    }

    /**
     * 타인의 정보 조회 API
     * @param userId
     * @return
     */
    @GetMapping("/v1/user/{id}")
    public GetOtherUserResponse getOtherUser(
            @AuthenticationPrincipal UserDetails myUserDetails,
            @PathVariable("id") Long userId) {
        User me = userService.getUserByEmail(myUserDetails.getUsername());
//        if (me.getId().equals(userId)) {
//            throw new RestApiException(UserErrorCode.OTHER_USER_IS_ME);
//        }
        User otherUser = userService.getUserById(userId);
        Integer followerCount = followService.getFollowerCount(otherUser.getEmail());
        Integer followingCount = followService.getFollowingCount(otherUser.getEmail());
        Boolean followed = followService.getFollowed(me, otherUser);

        CommonBadgeResponse badgeResponse = new CommonBadgeResponse(
                otherUser.getSelectedBadge().getBadgeName().getKrName(),
                otherUser.getSelectedBadge().getBadgeName().getDescription());
        return GetOtherUserResponse.builder()
                .nickname(otherUser.getNickname())
                .profileImgUrl(otherUser.getProfileImgUrl())
                .badge(badgeResponse)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .followed(followed)
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

        userService.updateSelectedBadge(user, badgeName.orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_BADGE_NAME)));

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/v1/user/location")
    public ResponseEntity postUserLocation(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PostUserLocationRequest postUserLocationRequest
    ) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        userService.updateLocation(user, postUserLocationRequest.getLatitude(), postUserLocationRequest.getLongitude());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/v1/user/fcm-token")
    public ResponseEntity postUserFcmToken(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PostUserFcmTokenRequest postUserFcmTokenRequest
    ) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        userService.updateFcmToken(user, postUserFcmTokenRequest.getFcmToken());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
