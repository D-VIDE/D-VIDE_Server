package com.divide.service;

import com.divide.dto.request.LoginRequest;
import com.divide.dto.request.SignupRequest;
import com.divide.dto.response.LoginResponse;
import com.divide.entity.User;
import com.divide.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getUsers() throws ExecutionException, InterruptedException {
        return userRepository.getUsers();
    }

    @Override
    public String signup(@RequestBody SignupRequest signupRequest) throws ExecutionException, InterruptedException {
        // TODO: 이메일 중복체크
        User user = new User(signupRequest.getEmail(), signupRequest.getPassword(), signupRequest.getProfileImgUrl(), signupRequest.getName(), signupRequest.getNickname());
        return userRepository.signup(user);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) throws ExecutionException, InterruptedException {
        Optional<User> result = userRepository.findByEmail(loginRequest.getEmail());
        if (result.isEmpty()) {
            return new LoginResponse(-1, "");
        }
        User user = result.get();
        boolean passwordMatched = user.getPassword().equals(loginRequest.getPassword());
        if (!passwordMatched) {
            return new LoginResponse(-2, "");
        }
        return new LoginResponse(0, user.getId());
    }
}
