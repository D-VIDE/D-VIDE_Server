package com.divide.service;

import com.divide.config.BaseException;
import com.divide.config.dto.request.LoginRequest;
import com.divide.config.dto.request.SignupRequest;
import com.divide.config.dto.response.LoginResponse;
import com.divide.entity.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    void signup(SignupRequest signupRequest) throws BaseException;
    LoginResponse login(LoginRequest loginRequest);
}
