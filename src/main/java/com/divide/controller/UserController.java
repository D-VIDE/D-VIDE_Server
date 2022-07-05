package com.divide.controller;

import com.divide.config.BaseException;
import com.divide.config.BaseResponse;
import com.divide.config.BaseResponseStatus;
import com.divide.config.dto.request.SignupRequest;
import com.divide.config.dto.response.GetUserResponse;
import com.divide.entity.User;
import com.divide.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @GetMapping("/user/all")
    public BaseResponse<List<GetUserResponse>> getUsers() {
        List<User> users = userService.getUsers();
        List<GetUserResponse> res = users.stream().map(user -> new GetUserResponse(user.getId(), user.getNickname())).toList();
        return new BaseResponse(res);
    }

    @PostMapping("/signup")
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
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public GetUserResponse getUser() {
        User user = userService.getMyUser();
        return null;
    }
}
