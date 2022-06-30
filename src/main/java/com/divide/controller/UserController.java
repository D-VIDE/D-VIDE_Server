package com.divide.controller;

import com.divide.dto.request.LoginRequest;
import com.divide.dto.request.SignupRequest;
import com.divide.dto.response.LoginResponse;
import com.divide.dto.response.UsersResponse;
import com.divide.dto.response.SignupResponse;
import com.divide.entity.User;
import com.divide.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<UsersResponse> getUsers() throws ExecutionException, InterruptedException {
        List<User> users = userService.getUsers();
        return ResponseEntity.ok().body(new UsersResponse(users));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) throws ExecutionException, InterruptedException {
        String userId = userService.signup(signupRequest);
        return ResponseEntity.ok().body(new SignupResponse(userId));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws ExecutionException, InterruptedException {
        if (loginRequest.getEmail().length() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(-3, ""));
        }
        if (loginRequest.getPassword().length() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(-4, ""));
        }

        LoginResponse loginResponse = userService.login(loginRequest);
        return ResponseEntity.ok().body(loginResponse);
    }
}
