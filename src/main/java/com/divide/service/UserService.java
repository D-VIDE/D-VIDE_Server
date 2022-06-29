package com.divide.service;

import com.divide.dto.request.LoginRequest;
import com.divide.dto.request.SignupRequest;
import com.divide.dto.response.LoginResponse;
import com.divide.entity.User;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserService {
    List<User> getUsers() throws ExecutionException, InterruptedException;

    String signup(SignupRequest signupRequest) throws ExecutionException, InterruptedException;
    LoginResponse login(LoginRequest loginRequest) throws ExecutionException, InterruptedException;
}
