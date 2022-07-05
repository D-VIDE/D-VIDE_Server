package com.divide.controller;

import com.divide.config.BaseException;
import com.divide.config.BaseResponse;
import com.divide.config.BaseResponseStatus;
import com.divide.config.dto.request.SignupRequest;
import com.divide.config.dto.response.GetUsersResponse;
import com.divide.entity.User;
import com.divide.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public BaseResponse<List<GetUsersResponse>> getUsers() {
        List<User> users = userService.getUsers();
        List<GetUsersResponse> res = users.stream().map(user -> new GetUsersResponse(user.getId(), user.getNickname())).toList();
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
}
