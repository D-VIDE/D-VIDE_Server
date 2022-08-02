package com.divide.user;

import com.divide.BaseException;
import com.divide.BaseResponseStatus;
import com.divide.user.dto.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    public void signup(SignupRequest signupRequest) throws BaseException {
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new BaseException(BaseResponseStatus.POST_USERS_EXISTS_EMAIL);
        }
        userRepository.signup(new User(
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()),
                signupRequest.getProfileImgUrl(),
                signupRequest.getNickname(),
                UserRole.USER
        ));
    }
}
