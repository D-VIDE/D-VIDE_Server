package com.divide.controller;

import com.divide.config.BaseException;
import com.divide.config.BaseResponse;
import com.divide.config.BaseResponseStatus;
import com.divide.config.dto.request.LoginRequest;
import com.divide.config.dto.request.SignupRequest;
import com.divide.config.dto.response.LoginResponse;
import com.divide.config.dto.response.SignupResponse;
import com.divide.entity.User;
import com.divide.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public BaseResponse getUsers() throws ExecutionException, InterruptedException {
        List<User> users = userService.getUsers();
        return new BaseResponse(users);
    }

    @PostMapping("/signup")
    public BaseResponse signup(@RequestBody @Valid SignupRequest signupRequest) throws BaseException {
        try {
            userService.signup(signupRequest);
        } catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
        return new BaseResponse(BaseResponseStatus.SUCCESS);
    }

    @PostMapping("/login")
    public BaseResponse login(@RequestBody LoginRequest loginRequest) throws ExecutionException, InterruptedException {
        if (loginRequest.getEmail().length() == 0) {
            return new BaseResponse(BaseResponseStatus.POST_USERS_EMPTY_EMAIL);
        }
        if (loginRequest.getPassword().length() == 0) {
            return new BaseResponse(BaseResponseStatus.POST_USERS_EMPTY_PASSWORD)
        }

        LoginResponse loginResponse = userService.login(loginRequest);
        return ResponseEntity.ok().body(loginResponse);
    }
}
