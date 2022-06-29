package com.divide.controller;

import com.divide.entity.User;
import com.divide.repository.UserRepository;
import com.divide.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
