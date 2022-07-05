package com.divide.service;

import com.divide.SecurityUtil;
import com.divide.config.BaseException;
import com.divide.config.BaseResponseStatus;
import com.divide.config.dto.request.SignupRequest;
import com.divide.entity.User;
import com.divide.entity.UserRole;
import com.divide.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    @Override
    public void signup(SignupRequest signupRequest) throws BaseException {
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new BaseException(BaseResponseStatus.POST_USERS_EXISTS_EMAIL);
        }
        userRepository.signup(new User(
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()),
                signupRequest.getProfileImgUrl(),
                signupRequest.getNickname(),
                UserRole.user
        ));
    }

    @Override
    @Transactional(readOnly = true)
    public User getMyUser() {
        User user = SecurityUtil.getCurrentEmail().flatMap(userRepository::findByEmail).orElse(null);
        return user;
    }
}
