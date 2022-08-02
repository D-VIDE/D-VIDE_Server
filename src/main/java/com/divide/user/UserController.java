package com.divide.user;

import com.divide.BaseException;
import com.divide.BaseResponse;
import com.divide.BaseResponseStatus;
import com.divide.user.dto.request.SignupRequest;
import com.divide.user.dto.response.GetUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @PostMapping("/user/signup")
    public BaseResponse signup(@RequestBody @Valid SignupRequest signupRequest) {
        try {
            userService.signup(signupRequest);
        } catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
        return new BaseResponse(BaseResponseStatus.SUCCESS);
    }

    /**
     * 현재 자신의 정보
     */
    @GetMapping("/user")
    public GetUserResponse getUser(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("userDetails = " + userDetails);

        User user = userService.getUserByEmail(userDetails.getUsername());
        return GetUserResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .badges(null)
                .followerCount(0)
                .followingCount(0)
                .savedPrice(user.getSavedMoney())
                .build();
    }
}
