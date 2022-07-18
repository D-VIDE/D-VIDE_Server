package com.divide.user;

import com.divide.BaseException;
import com.divide.user.dto.request.SignupRequest;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    void signup(SignupRequest signupRequest) throws BaseException;

    User getMyUser() throws BaseException;
}
