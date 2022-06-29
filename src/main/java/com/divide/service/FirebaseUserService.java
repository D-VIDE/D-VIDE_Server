package com.divide.service;

import com.divide.dto.request.SignupRequest;
import com.divide.entity.User;
import com.divide.repository.UserRepository;
import com.divide.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class FirebaseUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getUsers() throws ExecutionException, InterruptedException {
        return userRepository.getUsers();
    }

    @Override
    public void signup(@RequestBody SignupRequest signupRequest) throws ExecutionException, InterruptedException {
        userRepository.signup(signupRequest);
    }
}
