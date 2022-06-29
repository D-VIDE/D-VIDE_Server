package com.divide.controller;

import com.divide.entity.User;
import com.divide.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<Object> getUsers() throws ExecutionException, InterruptedException {
        List<User> list = userService.getUsers();
        return ResponseEntity.ok().body(list);
    }
}
