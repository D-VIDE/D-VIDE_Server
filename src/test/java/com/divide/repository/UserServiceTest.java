package com.divide.repository;

import com.divide.exception.RestApiException;
import com.divide.user.dto.request.SignupRequest;
import com.divide.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void testSignup() throws Exception {
        // given
        SignupRequest signupRequest1 = new SignupRequest("email1@gmail.com", "password1", "profileImgUrl1", "nickname1");
        SignupRequest signupRequest2 = new SignupRequest("email2@gmail.com", "password2", "profileImgUrl2", "nickname2");
        SignupRequest signupRequest3 = new SignupRequest("email3@gmail.com", "password3", "profileImgUrl3", "nickname3");
        SignupRequest signupRequest4 = new SignupRequest("email4@gmail.com", "password4", "profileImgUrl4", "nickname4");

        // when
        userService.signup(signupRequest1);
        userService.signup(signupRequest2);
        userService.signup(signupRequest3);
        userService.signup(signupRequest4);


        // then
        Assertions.assertThat(userRepository.getUsers().size()).isEqualTo(4);
    }

    @Test
    public void testSignupConflict() throws Exception {
        // given
        SignupRequest signupRequest1 = new SignupRequest("email1@gmail.com", "password1", "profileImgUrl1", "nickname1");
        SignupRequest signupRequest2 = new SignupRequest("email1@gmail.com", "password1", "profileImgUrl1", "nickname1");

        // when
        userService.signup(signupRequest1);
        
        // then
        org.junit.jupiter.api.Assertions.assertThrows(RestApiException.class, () -> {
            userService.signup(signupRequest2);
        });
    }
}
