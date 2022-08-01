package com.divide.user;

import com.divide.exception.RestApiException;
import com.divide.exception.code.UserErrorCode;
import com.divide.utils.SecurityUtil;
import com.divide.user.dto.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public void signup(SignupRequest signupRequest) {
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new RestApiException(UserErrorCode.DUPLICATED_USER);
        }
        userRepository.signup(new User(
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()),
                signupRequest.getProfileImgUrl(),
                signupRequest.getNickname(),
                UserRole.USER
        ));
    }

    @Override
    @Transactional(readOnly = true)
    public User getMyUser() {
        User user = SecurityUtil.getCurrentEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new RestApiException(UserErrorCode.DUPLICATED_USER));
        return user;
    }
}
