package com.divide.user;

import com.divide.user.dto.request.SignupRequest;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    void signup(SignupRequest signupRequest);

    User getMyUser();
}
